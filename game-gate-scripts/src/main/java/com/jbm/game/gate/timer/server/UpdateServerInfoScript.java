package com.jbm.game.gate.timer.server;

import java.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.script.ITimerEventScript;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.gate.server.GateServer;
import com.jbm.game.message.ServerMessage.ServerListRequest;
import com.jbm.game.message.ServerMessage.ServerRegisterRequest;

/**
 * 更新服务器信息脚本
 * @author JiangBangMing
 *
 * 2018年7月25日 下午12:43:03
 */
public class UpdateServerInfoScript implements ITimerEventScript{

	private static final Logger logger=LoggerFactory.getLogger(UpdateServerInfoScript.class);
	
	@Override
	public void secondHandler(LocalTime localTime) {
		if(localTime.getSecond()%5==0) {//每5秒更新一次
			//向服务器集群更新服务器信息
			MinaServerConfig minaServerConfig=GateServer.getInstance().getGateTcpUserServer().getMinaServerConfig();
			ServerRegisterRequest request=GateServer.getInstance().buildServerRegisterRequest(minaServerConfig);
			GateServer.getInstance().getClusterClient().sendMsg(request);
			
			//重连服务器检测
			GateServer.getInstance().getClusterClient().getMinaTcpClient().checkStatus();
			
			//请求游戏服务器列表
			ServerListRequest.Builder builder=ServerListRequest.newBuilder();
			builder.setServerType(ServerType.GAME.getType());
			GateServer.getInstance().getClusterClient().sendMsg(builder.build());
			
		}
	}
}
