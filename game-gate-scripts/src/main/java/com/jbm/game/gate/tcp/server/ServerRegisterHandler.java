package com.jbm.game.gate.tcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.engine.thread.ThreadType;
import com.jbm.game.gate.manager.ServerManager;
import com.jbm.game.message.ServerMessage;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.ServerMessage.ServerRegisterRequest;

/**
 * 游戏服循环注册更新
 * @author JiangBangMing
 *
 * 2018年7月25日 下午12:32:01
 */

@HandlerEntity(mid=MID.ServerRegisterReq_VALUE,msg=ServerRegisterRequest.class,thread=ThreadType.SYNC)
public class ServerRegisterHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(ServerRegisterHandler.class);
	
	@Override
	public void run() {
		ServerRegisterRequest req=getMsg();
		ServerMessage.ServerInfo serverInfo=req.getServerInfo();
		ServerInfo gameInfo=ServerManager.getInstance().getGameServerInfo(ServerType.valueof(serverInfo.getType()),serverInfo.getId());
		if(gameInfo==null) {
			logger.warn("网关服务没有{} 服:{}",ServerType.valueof(serverInfo.getType()).toString(),serverInfo.getId());
			return;
		}
		gameInfo.onIoSessionConnect(session);
		logger.info("服务器:{} 连接注册到网关服 {} ip:{}",gameInfo.getName(),getSession().getId(),getSession().getRemoteAddress().toString());
	}
}
