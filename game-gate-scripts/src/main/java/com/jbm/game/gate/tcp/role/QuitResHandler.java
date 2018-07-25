package com.jbm.game.gate.tcp.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.gate.manager.UserSessionManager;
import com.jbm.game.gate.struct.UserSession;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.hall.HallLoginMessage.QuitResponse;
import com.jbm.game.model.constant.Reason;

/**
 * 退出游戏
 * 
 * 在请求消息时已经移除了角色的连接会话信息,所有返回消息会话是游戏内部会话，不能从session中获取属性,不能关闭
 * @author JiangBangMing
 *
 * 2018年7月25日 上午11:19:51
 */
@HandlerEntity(mid=MID.QuitRes_VALUE,msg=QuitResponse.class)
public class QuitResHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(QuitResHandler.class);
	
	@Override
	public void run() {
		QuitResponse res=getMsg();
		UserSession userSession=UserSessionManager.getInstance().getUserSessionByRoleId(rid);
		if(userSession==null) {
			logger.warn("角色{}会话已遗失",rid);
			return;
		}
		
		if(userSession.getClientSession()!=null) {
			userSession.sendToClient(res);
			userSession.getClientSession().closeOnFlush();
			logger.info("{} 退出游戏",userSession.getRoleId());
		}
		
		//返回结果再移除，防止一些消息得不到转发失败
		UserSessionManager.getInstance().quit(userSession, Reason.UserQuit);
	}
}
