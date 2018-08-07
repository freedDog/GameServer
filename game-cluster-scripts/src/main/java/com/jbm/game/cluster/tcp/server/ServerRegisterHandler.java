package com.jbm.game.cluster.tcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.cluster.manager.ServerManager;
import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.engine.thread.ThreadType;
import com.jbm.game.message.ServerMessage;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.ServerMessage.ServerRegisterRequest;
import com.jbm.game.message.ServerMessage.ServerRegisterResponse;

/**
 * 注册服务器信息
 * @author JiangBangMing
 *
 * 2018年7月25日 下午2:37:18
 */
@HandlerEntity(mid=MID.ServerRegisterReq_VALUE,msg=ServerRegisterRequest.class,thread=ThreadType.SYNC)
public class ServerRegisterHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(ServerRegisterHandler.class);
	@Override
	public void run() {
		ServerRegisterRequest req=getMsg();
		ServerMessage.ServerInfo serverInfo=req.getServerInfo();
		logger.info("服务器{}_{}注册 ip:{}_port:{}",ServerType.valueof(serverInfo.getType()).toString(),serverInfo.getId(),serverInfo.getIp(),serverInfo.getPort());
		
		ServerInfo info=ServerManager.getInstance().registerServer(serverInfo, getSession());
		ServerRegisterResponse.Builder builder=ServerRegisterResponse.newBuilder();
		ServerMessage.ServerInfo.Builder infoBuilder=ServerMessage.ServerInfo.newBuilder();
		infoBuilder.mergeFrom(serverInfo);
		//反向更新
		infoBuilder.setState(info.getState());
		
		builder.setServerInfo(infoBuilder);
		getSession().write(builder.build());
		
	}
	
}
