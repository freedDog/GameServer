package com.jbm.game.cluster.http.server;

import java.util.ArrayList;
import java.util.List;

import com.jbm.game.cluster.manager.ServerManager;
import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.HttpHandler;
import com.jbm.game.engine.server.ServerInfo;

/**
 * 服务器列表
 * @author JiangBangMing
 *
 * 2018年7月25日 下午1:29:24
 */
@HandlerEntity(path="/server/list")
public class ServerListHandler extends HttpHandler{

	@Override
	public void run() {
		List<ServerInfo> servers=new ArrayList<>();
		ServerManager.getInstance().getServers().values().forEach(l -> l.forEach((id,serverInfo) ->{
			servers.add(serverInfo);
		}));
		sendMsg(servers);
	}
}
