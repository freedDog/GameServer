package com.jbm.game.gate.server.handler;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.mina.handler.ClientProtocolHandler;
import com.jbm.game.engine.server.BaseServerConfig;
import com.jbm.game.engine.server.Service;
import com.jbm.game.engine.util.MsgUtil;
import com.jbm.game.gate.struct.Config;
import com.jbm.game.gate.struct.UserSession;

/**
 * udp 消息处理器
 * @author JiangBangMing
 *
 * 2018年7月21日 下午5:30:59
 */
public class GateUdpUserServerHandler extends ClientProtocolHandler{

	private static final Logger logger=LoggerFactory.getLogger(GateUdpUserServerHandler.class);
	
	public GateUdpUserServerHandler() {
		super(8);
	}
	
	public GateUdpUserServerHandler(Service<MinaServerConfig> service) {
		this();
		this.service=service;
	}
	
	@Override
	protected void forward(IoSession session, int msgID, byte[] bytes) {
		//转发到大厅服
		if(msgID<Config.HALL_MAX_MID) {
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
			logger.warn("{}消息[{}]未找到玩家", MsgUtil.getIp(session), msgID);
			return;
		}
		if(!userSession.sendToGame(MsgUtil.clientToGame(msgID, bytes))) {
			logger.warn("角色[{}]没有连接游戏服务器,消息{}发送失败", userSession.getRoleId(),msgID);
		}
	}
	@Override
	public Service<? extends BaseServerConfig> getService() {
		return this.service;
	}
	
	@Override
	public void sessionOpened(IoSession session) {
		super.sessionOpened(session);
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
	}
	
	@Override
	public void sessionIdle(IoSession session, IdleStatus idleStatus) throws Exception {
		super.sessionIdle(session, idleStatus);
		session.closeNow();
	}
	
	/**
	 * 消息转发到大厅服或游戏服务器
	 * @param session
	 * @param msgID
	 * @param bytes
	 */
	private void forwardToHall(IoSession session,int msgID,byte[] bytes) {
		Object attribute=session.getAttribute(Config.USER_SESSION);
		if(attribute==null) {
			logger.warn("[{}]消息未找到对应的处理方式", msgID);
			return;
		}
		UserSession userSession=(UserSession)attribute;
		if(userSession.getRoleId()<=0) {
			logger.warn("{}消息[{}]未找到玩家", MsgUtil.getIp(session), msgID);
			return;
		}
		
		if(!userSession.sendToGame(MsgUtil.clientToGame(msgID, bytes))) {
			logger.warn("角色[{}]没有连接游戏服务器,消息{}发送失败", userSession.getRoleId(),msgID);
		}
	}
}
