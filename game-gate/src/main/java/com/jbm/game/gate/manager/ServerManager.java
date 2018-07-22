package com.jbm.game.gate.manager;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.engine.server.ServerState;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.engine.util.StringUtils;
import com.jbm.game.gate.struct.UserSession;

/**
 * 服务器管理类
 * @author JiangBangMing
 *
 * 2018年7月19日 下午2:44:44
 */
public class ServerManager {

	private static final Logger logger=LoggerFactory.getLogger(ServerManager.class);
	private static volatile ServerManager serverManager;
	
	private final Map<ServerType,Map<Integer,ServerInfo>> serverMap=new ConcurrentHashMap<>();
	
	private ServerManager() {
		
	}
	
	public static ServerManager getInstance() {
		if(serverManager==null) {
			synchronized (ServerManager.class) {
				if(serverManager==null) {
					serverManager=new ServerManager();
				}
			}
		}
		return serverManager;
	}
	
	/**
	 * 获取游戏消息
	 * @param serverType
	 * @param serverId
	 * @return
	 */
	public ServerInfo getGameServerInfo(ServerType serverType,int serverId) {
		if(serverMap.containsKey(serverType)) {
			return serverMap.get(serverType).get(serverId);
		}
		return null;
	}
	/**
	 * 更新服务器
	 */
	public void updateServerInfo() {
		// TODO 暂时不实现，等消息生成时在处理
	}
	/**
	 * h获取空闲的游戏服务器
	 * @param serverType
	 * @param userSession
	 * @return
	 */
	public ServerInfo getIdleGameServer(ServerType serverType,UserSession userSession) {
		Map<Integer, ServerInfo> map=serverMap.get(serverType);
		if(map==null||map.size()==0) {
			return null;
		}
		Optional<ServerInfo> findFirst=map.values().stream()
				.filter(server -> StringUtils.stringIsNullEmpty(userSession.getVersion())
						||userSession.getVersion().equals(server.getVersion()))//版本号
				.filter(server -> server.getState()==ServerState.NORMAL.ordinal())//状态
				.sorted((s1,s2) -> s1.getOnline()-s2.getOnline()).findFirst();
		if(findFirst.isPresent()) {
			return findFirst.get();
		}
		return null;
	}
	// TODO 在不实现 等消息生成完成时处理
	public void buildSystemErrorResponse() {
		
	}
}
