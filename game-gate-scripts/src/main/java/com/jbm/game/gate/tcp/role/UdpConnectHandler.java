package com.jbm.game.gate.tcp.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.gate.manager.UserSessionManager;
import com.jbm.game.gate.struct.Config;
import com.jbm.game.gate.struct.UserSession;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.system.SystemMessage.UdpConnectRequest;
import com.jbm.game.message.system.SystemMessage.UdpConnectResponse;

/**
 * 请求进行udp 连接
 * @author JiangBangMing
 *
 * 2018年7月25日 上午11:36:58
 */

@HandlerEntity(mid=MID.UdpConnectReq_VALUE,msg=UdpConnectRequest.class)
public class UdpConnectHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(UdpConnectHandler.class);
	
	@Override
	public void run() {
		UdpConnectRequest req=getMsg();
		logger.info("udp 连接:{}",req.toString());
		UserSession userSession=UserSessionManager.getInstance().getUserSessionByRoleId(req.getRid());
		UserSession userSession2=UserSessionManager.getInstance().getUserSessionBySessionId(req.getSessionId());
		UdpConnectResponse.Builder builder=UdpConnectResponse.newBuilder();
		builder.setResult(0);
		if(userSession==null) {
			builder.setResult(1);
		}else if(!userSession.equals(userSession2)) {
			builder.setResult(2);
		}
		
		userSession.setClientUdpSession(getSession());
		session.setAttribute(Config.USER_SESSION,userSession);
		
		session.write(builder.build());
	}
}
