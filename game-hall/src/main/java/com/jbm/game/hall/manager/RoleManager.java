package com.jbm.game.hall.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.util.JsonUtil;
import com.jbm.game.hall.script.IRoleScript;
import com.jbm.game.model.constant.Reason;
import com.jbm.game.model.struct.Role;

/**
 * 角色管理
 * @author JiangBangMing
 *
 * 2018年7月25日 下午3:47:10
 */
public class RoleManager {

	private static final Logger logger=LoggerFactory.getLogger(RoleManager.class);
	private static volatile RoleManager roleManager;
	
	/**role 数据需要实时存数据库*/
	private Map<Long, Role> roles=new ConcurrentHashMap<>();
	
	private RoleManager() {
		
	}
	public static RoleManager getInstance() {
		if(roleManager==null) {
			synchronized (RoleManager.class) {
				if(roleManager==null) {
					roleManager=new RoleManager();
				}
			}
		}
		return roleManager;
	}
	
	/**
	 * 创建角色
	 * @param userId
	 * @param roleConsumer
	 * @return
	 */
	public Role createUser(long userId,Consumer<Role> roleConsumer) {
		Collection<IRoleScript> evts=ScriptManager.getInstance().getBaseScriptEntry().getEvts(IRoleScript.class);
		Iterator<IRoleScript> iterator=evts.iterator();
		while(iterator.hasNext()) {
			IRoleScript userScript=iterator.next();
			Role role=userScript.createRole(userId, roleConsumer);
			if(role!=null) {
				return role;
			}
		}
		return null;
	}
	
	public Map<Long, Role> getRoles(){
		return roles;
	}
	
	public Role getRole(long id) {
		Role role=roles.get(id);
		Map<String, String> hgetAll=JedisManager.getJedisCluster().hgetAll(role.getRoleRedisKey());
		//从redis 读取最新数据
		if(hgetAll!=null&&role!=null) {
			JsonUtil.map2Object(hgetAll, role);
		}
		return role;
	}
	
	/**
	 * 登录
	 * @param role
	 * @param reason
	 */
	public void login(Role role,Reason reason) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class, 
				script -> script.login(role,reason));
	}
	
	/**
	 * 退出
	 * @param rid
	 * @param reason
	 */
	public void quit(long rid,Reason reason) {
		quit(getRole(rid),reason);
	}
	
	/**
	 * 退出游戏
	 * @param role
	 * @param reason
	 */
	public void quit(Role role,Reason reason) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class, 
				script -> script.quit(role, reason));
	}
}
