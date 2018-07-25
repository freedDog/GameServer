package com.jbm.game.cluster.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.cluster.manager.ServerManager;
import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.HttpHandler;
import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.engine.server.ServerState;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.engine.util.MsgUtil;
import com.jbm.game.model.constant.Config;

/**
 * 设置服务器状态
 * @author JiangBangMing
 *
 * 2018年7月25日 下午1:32:08
 */
@HandlerEntity(path="/server/state")
public class SetServerStateHandler extends HttpHandler {

	private static final Logger logger=LoggerFactory.getLogger(SetServerStateHandler.class);
	
	@Override
	public void run() {
		String auth=getString("auth");
		if(!Config.SERVER_AUTH.equals(auth)) {
			sendMsg("验证失败");
			return;
		}
		int serverType=getInt("serverType");
		int sereverId=getInt("serverId");
		int serverState=getInt("serverState");
		ServerInfo serverInfo=ServerManager.getInstance().getServer(ServerType.valueof(serverType), sereverId);
		if(serverInfo==null) {
			sendMsg(String.format("服务器 %d  %d 未启动", serverType,sereverId));
			return;
		}
		serverInfo.setState(serverState);
		logger.info("{} 设置服务器{}_{} 状态:{}",MsgUtil.getIp(getSession()),serverType,sereverId,serverState);
		sendMsg("服务器状态设置为:"+ServerState.valueOf(serverState));
	}
}
