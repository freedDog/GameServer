package com.jbm.game.gate.tcp.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.gate.manager.UserSessionManager;
import com.jbm.game.gate.struct.UserSession;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.hall.HallLoginMessage.QuitSubGameResponse;

/**
 * 退出子游戏返回
 * @author JiangBangMing
 *
 * 2018年7月25日 上午11:32:34
 */

@HandlerEntity(mid=MID.QuitSubGameRes_VALUE,msg=QuitSubGameResponse.class)
public class QuitSubGameResHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(QuitSubGameResHandler.class);
	
	@Override
	public void run() {
		QuitSubGameResponse res=getMsg();
		UserSession userSession=UserSessionManager.getInstance().getUserSessionByRoleId(rid);
		if(userSession==null) {
			logger.warn("角色{} 会话已遗失",rid);
			return;
		}
		
		if(userSession.getClientSession()!=null) {
			userSession.sendToClient(res);
			userSession.removeGame();
		}
		
		logger.info("角色{} 退出:{} ",userSession.getRoleId(),userSession.getServerType().toString());
	}
}
