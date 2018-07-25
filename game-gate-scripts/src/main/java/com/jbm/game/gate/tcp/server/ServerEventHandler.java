package com.jbm.game.gate.tcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.gate.manager.UserSessionManager;
import com.jbm.game.gate.script.IUserScript;
import com.jbm.game.gate.struct.UserSession;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.ServerMessage.ServerEventRequest;
import com.jbm.game.model.constant.Reason;

/**
 * 事件消息
 * @author JiangBangMing
 *
 * 2018年7月25日 下午12:24:44
 */
@HandlerEntity(mid=MID.ServerEventReq_VALUE,msg=ServerEventRequest.class)
public class ServerEventHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(ServerEventHandler.class);
	
	@Override
	public void run() {
		ServerEventRequest request=getMsg();
		switch (request.getType()) {
		case 1://gm踢玩家下线
			tickRole(request);
			break;

		default:
			break;
		}
		logger.info("处理事件{}",request.toString());
	}
	
	private void tickRole(ServerEventRequest request) {
		UserSession userSession=UserSessionManager.getInstance().getUserSessionByRoleId(request.getId());
		if(userSession==null) {
			return;
		}
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IUserScript.class, 
				script -> script.quit(userSession.getClientSession(), Reason.GmTickRole));
	}
}
