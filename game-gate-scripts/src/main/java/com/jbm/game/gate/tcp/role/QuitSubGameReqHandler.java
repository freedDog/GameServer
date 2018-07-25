package com.jbm.game.gate.tcp.role;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.engine.util.MsgUtil;
import com.jbm.game.gate.struct.Config;
import com.jbm.game.gate.struct.UserSession;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.hall.HallLoginMessage.QuitSubGameRequest;
import com.jbm.game.model.redis.key.HallKey;

/**
 * 退出子游戏
 * .
 *  处理用户session 请求处理，还是返回处理，根据客户端需求
 * @author JiangBangMing
 *
 * 2018年7月25日 上午11:25:01
 */
@HandlerEntity(mid=MID.QuitSubGameReq_VALUE,msg=QuitSubGameRequest.class)
public class QuitSubGameReqHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(QuitSubGameReqHandler.class);
	
	@Override
	public void run() {
		QuitSubGameRequest req=getMsg();
		Object attribute=getSession().getAttribute(Config.USER_SESSION);
		if(attribute==null) {
			logger.warn("{} 无用户会话",MsgUtil.getIp(getSession()));
			return;
		}
		if(req==null) {
			req=QuitSubGameRequest.newBuilder().build();
		}
		
		UserSession userSession=(UserSession)attribute;
		userSession.sendToGame(req);
		userSession.removeGame();
		
		//清空角色服务器ID,服务类型，网关统一处理，避免游戏服重复处理
		String key=HallKey.Role_Map_Info.getKey(userSession.getRoleId());
		Map<String, String> redisMap=new HashMap<>();
		redisMap.put("gameType",ServerType.NONE.toString());
		redisMap.put("gameId", String.valueOf(0));
		JedisManager.getJedisCluster().hmset(key, redisMap);
	}
}
