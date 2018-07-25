package com.jbm.game.cluster.http.server;

import com.jbm.game.cluster.manager.ServerManager;
import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.HttpHandler;
import com.jbm.game.engine.server.ServerInfo;

/**
 * 获取网关服务器
 * @author JiangBangMing
 *
 * 2018年7月25日 下午1:20:23
 */

@HandlerEntity(path="/server/gate/ip")
public class GetGateIpHandler extends HttpHandler{
	
	@Override
	public void run() {
		try {
			String version =getString("version");
			ServerInfo serverInfo=ServerManager.getInstance().getIdleGate(version);
			if(serverInfo==null) {
				getParameter().appendBody("无可用网关服");
			}else {
				getParameter().appendBody(serverInfo.getIp()+":"+serverInfo.getPort());
			}
		}finally {
			response();
		}
	}
}
