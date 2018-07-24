package com.jbm.game.gate.server.handler;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.ssl.SslFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.mina.handler.ClientProtocolHandler;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.server.BaseServerConfig;
import com.jbm.game.engine.server.Service;
import com.jbm.game.engine.util.MsgUtil;
import com.jbm.game.gate.script.IUserScript;
import com.jbm.game.gate.server.ssl.GateSslContextFactory;
import com.jbm.game.gate.struct.Config;
import com.jbm.game.gate.struct.UserSession;
import com.jbm.game.model.constant.Reason;

/**
 * 大厅tcp 消息处理器
 * @author JiangBangMing
 *
 * 2018年7月21日 下午2:26:13
 */
public class GateTcpUserServerHandler extends ClientProtocolHandler{

	private static final Logger logger=LoggerFactory.getLogger(GateTcpUserServerHandler.class);
	
	public GateTcpUserServerHandler() {
		super(0);
	}
	
	public GateTcpUserServerHandler(Service<MinaServerConfig> service) {
		super(8);
		this.service=service;
	}
	
	/**
	 * 消息转发到大厅服或游戏服务器
	 */
	@Override
	protected void forward(IoSession session, int msgID, byte[] bytes) {
		//转发到大厅服
		if(msgID< Config.HALL_MAX_MID) {
			forwardToHall(session, msgID, bytes);
			return;
		}
		//转发到游戏服
		Object attribute=session.getAttribute(Config.USER_SESSION);
		if(attribute==null) {
			logger.warn("[{}]消息未找到对应的处理方式", msgID);
			return;
		}
		UserSession userSession=(UserSession)attribute;
		if(userSession.getRoleId()<=0) {
			logger.warn("{} 消息 [{}] 未找到玩家",MsgUtil.getIp(session),msgID);
			return;
		}
		if(userSession.sendToGame(MsgUtil.clientToGame(msgID, bytes))) {
			return;
		}
		logger.warn("角色[{}] 没有连接游戏服务器，消息{}发送失败",userSession.getRoleId(),msgID);
	}
	
	@Override
	public Service<? extends BaseServerConfig> getService() {
		return this.service;
	}
	
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		if(Config.USE_SSL) {
			try {
				SslFilter sslFilter=new SslFilter(GateSslContextFactory.getInstance(true));
				session.getFilterChain().addFirst("SSL",sslFilter);
			}catch (Exception e) {
				logger.error("创建SSL",e);
				throw new RuntimeException(e); 
			}
		}
	}
	
	@Override
	public void messageReceived(IoSession session, Object obj) throws Exception {
		super.messageReceived(session, obj);
	}
	
	@Override
	public void sessionOpened(IoSession session) {
		super.sessionOpened(session);
		UserSession userSession=new UserSession(session);
		session.setAttribute(Config.USER_SESSION,userSession);
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IUserScript.class,script -> script.quit(session, Reason.ServerClose));
	}
	
	@Override
	public void sessionIdle(IoSession session, IdleStatus idleStatus) throws Exception {
		super.sessionIdle(session, idleStatus);
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IUserScript.class, script->script.quit(session, Reason.SessionIdle));
	}
	
	private void forwardToHall(IoSession session,int msgID,byte[] bytes) {
		Object attribute=session.getAttribute(Config.USER_SESSION);
		if(attribute==null) {
			logger.warn("[{}]消息未找到对应的处理方式", msgID);
			return;
		}
		UserSession userSession = (UserSession) attribute;
		if(userSession.getRoleId()<=0) {
			logger.warn("{} 消息 [{}] 未找到玩家",MsgUtil.getIp(session),msgID);
			return;
		}
		if (!userSession.sendToHall(MsgUtil.clientToGame(msgID, bytes))) {
			logger.warn("角色[{}] 没有连接大厅服务器", userSession.getRoleId());
			return;
		}
	}
}
