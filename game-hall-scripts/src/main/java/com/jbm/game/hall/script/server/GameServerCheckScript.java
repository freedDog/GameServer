package com.jbm.game.hall.script.server;

import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.hall.manager.RoleManager;
import com.jbm.game.hall.server.HallServer;
import com.jbm.game.message.ServerMessage.ServerInfo.Builder;
import com.jbm.game.model.script.IGameServerCheckScript;

/**
 * 服务器状态监测脚本
 * @author JiangBangMing
 *
 * 2018年8月6日 上午11:48:02
 */
public class GameServerCheckScript implements IGameServerCheckScript{

	@Override
	public void buildServerInfo(Builder builder) {
		IGameServerCheckScript.super.buildServerInfo(builder);
		MinaServerConfig minaServerConfig=HallServer.getInstance().getHallHttpServer().getMinaServerConfig();
		builder.setHttpport(minaServerConfig.getHttpPort());
		builder.setIp(minaServerConfig.getIp());
		builder.setOnline(RoleManager.getInstance().getRoles().size());//设置在线人数
	}
}
