package com.jbm.game.gate.tcp.role;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.gate.manager.ServerManager;
import com.jbm.game.gate.manager.UserSessionManager;
import com.jbm.game.gate.struct.UserSession;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.hall.HallLoginMessage.LoginSubGameRequest;
import com.jbm.game.message.system.SystemMessage.SystemErrorCode;
import com.jbm.game.message.system.SystemMessage.SystemErrorResponse;
import com.jbm.game.model.redis.key.HallKey;

/**
 * 登录子游戏
 * @author JiangBangMing
 *
 * 2018年7月25日 上午10:55:58
 */
@HandlerEntity(mid=MID.LoginSubGameReq_VALUE,msg=LoginSubGameRequest.class)
public class LoginSubGameHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(LoginSubGameHandler.class);
	
	@Override
	public void run() {
		LoginSubGameRequest request=getMsg();
		long rid=request.getRid();
		ServerType serverType=ServerType.valueof(request.getGameType());
		logger.info("[{}]登录游戏{}",rid,serverType);
		
		UserSession userSession=UserSessionManager.getInstance().getUserSessionByUserId(rid);
		userSession.setServerType(serverType);
		ServerInfo serverInfo=null;
		
		//重连登录到之前保留的游戏服
		String key=HallKey.Role_Map_Info.getKey(userSession.getRoleId());
		String gameType=JedisManager.getJedisCluster().hget(key, "gameType");
		if(gameType!=null&&gameType.equals(serverType.toString())) {
			String gameId=JedisManager.getJedisCluster().hget(key, "gameId");
			if(gameId!=null) {
				serverInfo=ServerManager.getInstance().getGameServerInfo(serverType, Integer.parseInt(gameId));
			}
		}
		//随机选择一个空闲的服务器
		if(serverInfo==null) {
			serverInfo=ServerManager.getInstance().getIdleGameServer(serverType, userSession);
		}
		if(serverInfo==null) {
			SystemErrorResponse response=ServerManager.getInstance().buildSystemErrorResponse(SystemErrorCode.GameNotFind, "游戏服服务器维护中");
			userSession.getClientSession().write(response);
			logger.warn("{} 没有可用服务器",serverType.toString());
			return;
		}
		userSession.setServerType(serverType);
		userSession.setServerId(serverInfo.getId());
		
		//设置玩家登录游戏服属性
		Map<String, String> redisMap=new HashMap<>(2);
		redisMap.put("gameId",String.valueOf(serverInfo.getId()));
		redisMap.put("gameType", String.valueOf(serverType.getType()));
		JedisManager.getJedisCluster().hmset(key, redisMap);
		
		userSession.sendToGame(request);
		
		
	}
}
