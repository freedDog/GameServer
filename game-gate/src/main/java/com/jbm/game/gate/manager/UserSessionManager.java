package com.jbm.game.gate.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.gate.script.IUserScript;
import com.jbm.game.gate.struct.UserSession;
import com.jbm.game.model.constant.Reason;

/**
 * 用户连接会话管理类
 * @author JiangBangMing
 *
 * 2018年7月19日 下午8:46:23
 */
public class UserSessionManager {

	private static final Logger logger=LoggerFactory.getLogger(UserSessionManager.class);
	private static volatile UserSessionManager userSessionManager;
	
	//用户session key:sessionID
	private Map<Long, UserSession> allSessions=new ConcurrentHashMap<>();
	//用户session key userID
	private Map<Long, UserSession> userSessions=new ConcurrentHashMap<>();
	//用户session key:roleID
	private Map<Long, UserSession> roleSessions=new ConcurrentHashMap<>();
	private UserSessionManager() {
		
	}
	
	public static UserSessionManager getInstance() {
		if(userSessionManager==null) {
			synchronized (UserSessionManager.class) {
				if(userSessionManager==null) {
					userSessionManager=new UserSessionManager();
				}
			}
		}
		return userSessionManager;
	}
	
	/**
	 * 用户连接服务器
	 * @param userSession
	 */
	public void onUserConnected(UserSession userSession) {
		if(userSession.getClientSession()!=null) {
			allSessions.put(userSession.getClientSession().getId(), userSession);
		}
	}
	/**
	 * 登录大厅
	 * @param userSession
	 * @param userId
	 * @param roleId
	 */
	public void logginHallSuccess(UserSession userSession,long userId,long roleId) {
		userSession.setUserId(userId);
		userSession.setRoleId(roleId);
		
		userSessions.put(userId, userSession);
		roleSessions.put(roleId, userSession);
	}
	
	public UserSession getUserSessionByUserId(long userId) {
		return userSessions.get(userId);
	}
	
	public UserSession getUserSessionByRoleId(long roleId) {
		return roleSessions.get(roleId);
	}
	
	/**
	 * 用户session
	 * @param sessionId
	 * @return
	 */
	public UserSession getUserSessionBySessionId(long sessionId) {
		return allSessions.get(sessionId);
	}
	
	/**
	 * 用户session 离开
	 * @param userSession
	 */
	public void quit(UserSession userSession,Reason reason) {
		allSessions.remove(userSession.getClientSession().getId());
		userSessions.remove(userSession.getUserId());
		roleSessions.remove(userSession.getRoleId());
	}
	
	/**
	 * 广播消息给前端客户端
	 * @param message
	 */
	public void broadcast(Message message) {
		this.allSessions.values().forEach(session -> session.sendToClient(message));
	}
	/**
	 * 所有在线人数
	 * @return
	 */
	public int getOnlineCount() {
		return allSessions.size();
	}
	
	public void onShutdown() {
		//提出玩家
		for(UserSession userSession:allSessions.values()) {
			ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IUserScript.class, script -> script.quit(userSession.getClientSession(),Reason.ServerClose));
		} 
	}
}
