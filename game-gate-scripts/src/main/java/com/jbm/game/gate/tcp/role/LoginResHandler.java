package com.jbm.game.gate.tcp.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.thread.ThreadType;
import com.jbm.game.gate.manager.ServerManager;
import com.jbm.game.gate.manager.UserSessionManager;
import com.jbm.game.gate.struct.UserSession;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.hall.HallLoginMessage.LoginResponse;
import com.jbm.game.message.system.SystemMessage.SystemErrorCode;
import com.jbm.game.model.redis.key.HallKey;

/**
 * 登录返回
 * <p>处理用户的连接session,设置userSession用户ID,角色ID</p>
 * @author JiangBangMing
 *
 * 2018年7月25日 上午10:47:20
 */

@HandlerEntity(mid=MID.LoginRes_VALUE,desc="登录",thread=ThreadType.IO,msg=LoginResponse.class)
public class LoginResHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(LoginResHandler.class);
	
	@Override
	public void run() {
		LoginResponse response=getMsg();
		UserSession userSession=UserSessionManager.getInstance().getUserSessionBySessionId(response.getSessionId());
		if(userSession==null) {
			session.write(
					ServerManager.getInstance().buildSystemErrorResponse(SystemErrorCode.ConectReset,"连接会话失效，请重连"));
			logger.warn("连接会话已失效，请重连");
			return;
		}
		String key=HallKey.Role_Map_Info.getKey(userSession.getRoleId());
		JedisManager.getJedisCluster().hset(key, "hallId",String.valueOf(userSession.getHallServerId()));
		UserSessionManager.getInstance().logginHallSuccess(userSession, response.getUid(), response.getRid());
		userSession.sendToClient(response);
	}
}
