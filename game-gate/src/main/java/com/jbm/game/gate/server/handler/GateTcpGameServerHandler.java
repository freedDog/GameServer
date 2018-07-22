package com.jbm.game.gate.server.handler;

import java.util.Arrays;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.mina.handler.DefaultProtocolHandler;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.server.BaseServerConfig;
import com.jbm.game.engine.server.Service;
import com.jbm.game.engine.util.MsgUtil;
import com.jbm.game.gate.manager.UserSessionManager;
import com.jbm.game.gate.script.IGateServerScript;
import com.jbm.game.gate.struct.UserSession;

/**
 * 游戏服，大厅服等内部共用的服务器
 * @author JiangBangMing
 *
 * 2018年7月21日 下午2:13:30
 */
public class GateTcpGameServerHandler extends DefaultProtocolHandler{

	private static final Logger logger=LoggerFactory.getLogger(GateTcpGameServerHandler.class);
	private Service<MinaServerConfig> service;
	
	public GateTcpGameServerHandler(Service<MinaServerConfig> service) {
		super(12);
		this.service=service;
	}
	
	/**
	 * 将消息转发给游戏客户端
	 */
	@Override
	protected void forward(IoSession session, int msgID, byte[] bytes) {
		long rid=MsgUtil.getMessageRID(bytes, 0);
		if(rid<=0) {
			logger.warn("消息[{}] 未找到角色{}",msgID,rid);
			return;
		}
		UserSession userSession=UserSessionManager.getInstance().getUserSessionByRoleId(rid);
		if(userSession==null) {
			return;
		}
		if(userSession.getClientSession()==null) {
			return;
		}
		//udp 转发
		if(ScriptManager.getInstance().getBaseScriptEntry().predicateScripts(IGateServerScript.class,(IGateServerScript script) ->
					script.isUdpMsg(userSession.getServerType(), msgID))) {
			userSession.sendToClientUdp(Arrays.copyOfRange(bytes, 8, bytes.length));
			return;
		}
		//tcp 返回
		userSession.sendToClient(Arrays.copyOfRange(bytes, 8, bytes.length));//前8字节为角色ID;
	}
	
	@Override
	public Service<? extends BaseServerConfig> getService() {
		return this.service;
	}
	
	@Override
	public void sessionOpened(IoSession session) {
		super.sessionOpened(session);
	}
}
