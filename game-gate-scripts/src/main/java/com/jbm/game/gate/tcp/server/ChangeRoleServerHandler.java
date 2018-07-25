package com.jbm.game.gate.tcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.gate.manager.ServerManager;
import com.jbm.game.gate.manager.UserSessionManager;
import com.jbm.game.gate.struct.UserSession;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.ServerMessage.ChangeRoleServerRequest;
import com.jbm.game.message.ServerMessage.ChangeRoleServerResponse;
import com.jbm.game.message.hall.HallLoginMessage.LoginSubGameRequest;

/**
 * 修改角色在网关服所连接的其他服务器信息
 * @author JiangBangMing
 *
 * 2018年7月25日 上午11:54:38
 */
@HandlerEntity(mid=MID.ChangeRoleServerReq_VALUE,msg=ChangeRoleServerRequest.class)
public class ChangeRoleServerHandler extends TcpHandler{

	
	private static final Logger logger=LoggerFactory.getLogger(ChangeRoleServerHandler.class);
	
	@Override
	public void run() {
		ChangeRoleServerRequest req=getMsg();
		ChangeRoleServerResponse.Builder builder=ChangeRoleServerResponse.newBuilder();
		builder.setResult(0);
		long roleId=req.getRoleId();
		UserSession userSession=UserSessionManager.getInstance().getUserSessionByRoleId(roleId);
		logger.info("jues{} 进行跨服",roleId);
		if(userSession==null) {
			builder.setResult(1);
		}else {
			ServerType serverType=ServerType.valueof(req.getServerType());
			ServerInfo serverInfo=null;
			if(req.getServerId()<1) {//找空闲服务器
				serverInfo=ServerManager.getInstance().getIdleGameServer(serverType, userSession);
			}else {
				serverInfo=ServerManager.getInstance().getGameServerInfo(serverType, req.getServerId());
			}
			if(serverInfo==null) {
				builder.setResult(2);
			}else {
				if(serverType==ServerType.HALL) {
					userSession.setHallServerId(serverInfo.getId());
					userSession.setHallSession(serverInfo.getMostIdleIoSession());
					//TODO 重发连接大厅消息
				}else {
					userSession.setGameSession(serverInfo.getMostIdleIoSession());
					userSession.setServerId(serverInfo.getId());
					userSession.setServerType(serverType);
					//发送登录消息
					LoginSubGameRequest.Builder loginGameBuilder=LoginSubGameRequest.newBuilder();
					loginGameBuilder.setGameType(serverType.getType());
					loginGameBuilder.setRid(roleId);
					loginGameBuilder.setType(2);
					userSession.sendToGame(loginGameBuilder.build());
				}
			}
		}
		logger.info("跨服结果:{}",builder.build().toString());
		sendIdMsg(builder.build());
	}
}
