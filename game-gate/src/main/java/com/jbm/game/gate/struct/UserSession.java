package com.jbm.game.gate.struct;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.mina.message.IDMessage;
import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.gate.manager.ServerManager;
import com.jbm.game.gate.manager.UserSessionManager;

/**
 * 用户连接会话
 * @author JiangBangMing
 *
 * 2018年7月19日 下午2:04:27
 */
public class UserSession {

	private static final Logger logger=LoggerFactory.getLogger(UserSession.class);
	//用户ID
	private long userId;
	//角色ID
	private long roleId;
	//游戏前段用户session
	private IoSession clientSession;
	//游戏前段udp session
	private IoSession clientUdpSession;
	//当前连接的子游戏session
	private IoSession gameSession;
	//当前连接大厅session
	private IoSession hallSession;
	//登录的游戏类型
	private ServerType serverType;
	///登录的大厅id
	private int serverId;
	//登录的大厅ID
	private int hallServerId;
	//客户端使用服务器的版本号
	private String version;
	
	public UserSession(IoSession clientSession) {
		super();
		this.clientSession=clientSession;
		UserSessionManager.getInstance().onUserConnected(this);
	}
	
	/**
	 * 发送给游戏客户端
	 * @param msg
	 * @return
	 */
	public boolean sendToClient(Object msg) {
		try {
			if(getClientSession()!=null&&getClientSession().isConnected()) {
				getClientSession().write(msg);
				return true;
			}
		}catch (Exception e) {
			logger.error("sendToClient:",e);
		}
		return false;
	}
	/**
	 * 发送给游戏客户端,udp
	 * @param msg
	 * @return
	 */
	public boolean sendToClientUdp(Object msg) {
		try {
			if(getClientUdpSession()!=null&&getClientUdpSession().isConnected()) {
				getClientUdpSession().write(msg);
				return true;
			}
		}catch (Exception e) {
			logger.error("sendToClientUdp:",e);
		}
		return false;
	}
	/**
	 * 发送给游戏客户端
	 * @param msg
	 * @return
	 */
	public boolean sendToGame(Object msg) {
		try {
			if(getUserId()<1) {
				return false;
			}
			if(getGameSession()!=null&&getGameSession().isConnected()) {
				IDMessage idMessage=new IDMessage(getGameSession(), msg, roleId<1 ?userId:roleId);
				idMessage.run();
				return true;
			}
		}catch (Exception e) {
			logger.error("sendToGame:",e);
		}
		return false;
	}
	
	/**
	 * 发送给游戏客户端
	 * @param msg
	 * @return
	 */
	public boolean sendToHall(Object msg) {
		try {
			if(getUserId()<1) {
				return false;
			}
			if(getHallSession()!=null&&getHallSession().isConnected()) {
				IDMessage idMessage=new IDMessage(getHallSession(), msg, roleId<1? userId:roleId);
				idMessage.run();
				return true;
			}
		}catch (Exception e) {
			logger.error("sendToHall:",e);
		}
		return false;
	}

	/**
	 * 移除游戏连接状态
	 */
	public void removeGame() {
		this.setGameSession(null);
		this.setServerId(0);
		this.setServerType(null);
	}
	
	public void removeHall() {
		this.setHallSession(null);
		this.setHallServerId(0);
	}
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public IoSession getClientSession() {
		return clientSession;
	}
	public void setClientSession(IoSession clientSession) {
		this.clientSession = clientSession;
	}
	public IoSession getClientUdpSession() {
		return clientUdpSession;
	}
	public void setClientUdpSession(IoSession clientUdpSession) {
		this.clientUdpSession = clientUdpSession;
	}
	public IoSession getGameSession() {
		if((gameSession==null||!gameSession.isConnected())&&getServerId()>0) {
			ServerInfo serverInfo=ServerManager.getInstance().getGameServerInfo(serverType, serverId);
			if(serverInfo!=null) {
				gameSession=serverInfo.getMostIdleIoSession();
			}else {
				logger.warn("{}---{} 没有服务器连接",serverType.toString(),serverId);
			}
		}
		return gameSession;
	}
	public void setGameSession(IoSession gameSession) {
		this.gameSession = gameSession;
	}
	public IoSession getHallSession() {
		if(hallSession==null||!hallSession.isConnected()) {
			ServerInfo serverInfo=ServerManager.getInstance().getGameServerInfo(ServerType.HALL, hallServerId);
			if(serverInfo!=null) {
				hallSession=serverInfo.getMostIdleIoSession();
			}
		}
		return hallSession;
	}
	public void setHallSession(IoSession hallSession) {
		this.hallSession = hallSession;
	}
	public ServerType getServerType() {
		return serverType;
	}
	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	
	
	public int getHallServerId() {
		return hallServerId;
	}
	public void setHallServerId(int hallServerId) {
		this.hallServerId = hallServerId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
}
