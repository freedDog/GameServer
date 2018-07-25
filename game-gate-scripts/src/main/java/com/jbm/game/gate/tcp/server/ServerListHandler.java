package com.jbm.game.gate.tcp.server;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.gate.manager.ServerManager;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.ServerMessage.ServerListResponse;

/**
 * 返回游戏服务器列表
 * @author JiangBangMing
 *
 * 2018年7月25日 下午12:29:35
 */

@HandlerEntity(mid=MID.ServerListRes_VALUE,msg=ServerListResponse.class)
public class ServerListHandler extends TcpHandler{

	@Override
	public void run() {
		ServerListResponse response=getMsg();
		if(response==null) {
			return;
		}
		response.getServerInfoList().forEach(info ->
				ServerManager.getInstance().updateServerInfo(info));
	}
}
