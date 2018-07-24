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
import com.jbm.game.message.ServerMessage;
import com.jbm.game.message.system.SystemMessage.SystemErrorCode;
import com.jbm.game.message.system.SystemMessage.SystemErrorResponse;

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
	public void updateServerInfo(ServerMessage.ServerInfo info) {
		if(info.getType()<100&&info.getType()!=ServerType.HALL.ordinal()) {
			//游戏服从101开始定义
			//大厅服需要更新
			return;
		}
		ServerType serverType=ServerType.valueof(info.getType());
		ServerInfo serverInfo=getGameServerInfo(serverType, info.getId());
		if(serverInfo==null) {
			serverInfo=new ServerInfo();
			serverInfo.setId(info.getId());
		}
		serverInfo.setIp(info.getIp());
		serverInfo.setPort(info.getPort());
		serverInfo.setOnline(info.getOnline());
		serverInfo.setMaxUserCount(info.getMaxUserCount());
		serverInfo.setName(info.getName());
		serverInfo.setHttpPort(info.getHttpport());
		serverInfo.setState(info.getState());
		serverInfo.setType(info.getType());
		serverInfo.setWwwip(info.getWwwip());
		
		if(!serverMap.containsKey(serverType)) {
			serverMap.put(serverType, new ConcurrentHashMap<>());
		}
		serverMap.get(serverType).put(serverInfo.getId(), serverInfo);
		logger.info("游戏服务器{}注册更新到网关服务器",serverInfo.toString());
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
	/**
	 * 构建错误消息
	 */
	public SystemErrorResponse buildSystemErrorResponse(SystemErrorCode errorCode,String msg) {
		SystemErrorResponse.Builder builder=SystemErrorResponse.newBuilder();
		builder.setErrorCode(errorCode);
		if(msg!=null) {
			builder.setMsg(msg);
		}
		return builder.build();
	}
}
