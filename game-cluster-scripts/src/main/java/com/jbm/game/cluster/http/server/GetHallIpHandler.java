package com.jbm.game.cluster.http.server;

import java.util.Map;
import java.util.Optional;
import com.jbm.game.cluster.manager.ServerManager;
import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.HttpHandler;
import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.engine.server.ServerState;
import com.jbm.game.engine.server.ServerType;

/**
 * 获取大厅服务器
 * @author JiangBangMing
 *
 * 2018年7月25日 下午1:23:27
 */
@HandlerEntity(path="/server/hall/ip")
public class GetHallIpHandler extends HttpHandler{

	@Override
	public void run() {
		try {
			Map<Integer, ServerInfo> servers=ServerManager.getInstance().getServers(ServerType.HALL);
			if(servers==null||servers.size()<1) {
				getParameter().appendBody("无可用大厅服");
			}else {
				Optional<ServerInfo> findFirst=servers.values().stream()
						.filter(server ->  server.getState()==ServerState.NORMAL.ordinal()&&server.getSession()!=null
						&&server.getSession().isConnected())
						.sorted((s1,s2) -> s1.getOnline()-s2.getOnline()).findFirst();
				if(findFirst.isPresent()) {
					getParameter().appendBody(findFirst.get().getIp()+":"+findFirst.get().getPort());
				}else {
					getParameter().appendBody("无可用大厅服");
				}
			}
		}finally {
			response();
		}
	}
}
