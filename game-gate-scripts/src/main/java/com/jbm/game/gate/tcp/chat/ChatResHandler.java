package com.jbm.game.gate.tcp.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.gate.manager.UserSessionManager;
import com.jbm.game.gate.struct.UserSession;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.hall.HallChatMessage.ChatResponse;

/**
 * 聊天消息返回
 * @author JiangBangMing
 *
 * 2018年7月24日 下午7:20:12
 */

@HandlerEntity(mid=MID.ChatRes_VALUE,msg=ChatResponse.class)
public class ChatResHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(ChatResHandler.class);
	
	@Override
	public void run() {
		ChatResponse res=getMsg();
		switch (res.getChatType()) {
		case PRIVATE://在当前网关则转发
			UserSession userSession=UserSessionManager.getInstance().getUserSessionByRoleId(this.rid);
			if(userSession!=null) {
				userSession.sendToClient(res);
			}
			break;
		case WORLD: //广播所有玩家
		case PMD:
			UserSessionManager.getInstance().broadcast(res);
			break;

		default:
			break;
		}
	}
}
