package com.jbm.game.bydr.script.server;

import com.jbm.game.bydr.manager.RoleManager;
import com.jbm.game.bydr.server.BydrServer;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.message.ServerMessage.ServerInfo.Builder;
import com.jbm.game.model.script.IGameServerCheckScript;

/**
 * 服务器状态监测脚本
 * @author JiangBangMing
 *
 * 2018年8月3日 下午2:57:58
 */
public class GameServerCheckScript implements IGameServerCheckScript{

	@Override
	public void buildServerInfo(Builder builder) {
		IGameServerCheckScript.super.buildServerInfo(builder);
		MinaServerConfig minaServerConfig=BydrServer.getInstance().getGameHttpService().getMinaServerConfig();;
		builder.setHttpport(minaServerConfig.getHttpPort());
		builder.setIp(minaServerConfig.getIp());
		builder.setOnline(RoleManager.getInstance().getOnlineRoles().size());//设置在线人数
	}
}
