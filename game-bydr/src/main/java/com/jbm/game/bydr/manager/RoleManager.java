package com.jbm.game.bydr.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.script.IRoleScript;
import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.util.JsonUtil;
import com.jbm.game.model.constant.Reason;
import com.jbm.game.model.redis.key.BydrKey;

/**
 * 角色管理
 * @author JiangBangMing
 *
 * 2018年8月2日 下午5:16:51
 */
public class RoleManager {

	private static final Logger logger=LoggerFactory.getLogger(RoleManager.class);
	private static volatile RoleManager roleManager;
	
	private Map<Long, Role> onlineRoles=new ConcurrentHashMap<>();//在线的角色
	
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

	public Map<Long, Role> getOnlineRoles() {
		return onlineRoles;
	}
	
	public Role getRole(long roleId) {
		return onlineRoles.get(roleId);
	}
	
	/**
	 * 登录
	 * @param roleId
	 * @param reason
	 * @param roleConsumer
	 */
	public void login(long roleId,Reason reason,Consumer<Role> roleConsumer) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class, script -> script.login(roleId, reason, roleConsumer));
	}
	/**
	 * 退出
	 * @param role
	 * @param reason
	 */
	public void quit(Role role,Reason reason) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class, script -> script.quit(role, reason));
		
	}
	/**
	 * 加载角色数据
	 * @param roleId
	 * @return
	 */
	public Role loadRoleData(long roleId) {
		Map<String, String> roleMap=JedisManager.getJedisCluster().hgetAll(BydrKey.Role_Map.getKey(roleId));
		if(roleMap==null||roleMap.isEmpty()) {
			return null;
		}
		Role role=new Role();
		JsonUtil.map2Object(roleMap, role);
		return role;
	}
	
	/**
	 * 存储角色数据
	 * @param role
	 */
	public void saveRoleData(Role role) {
		String key=BydrKey.Role_Map.getKey(role.getId());
		logger.info("{} 存储数据",key);
		JedisManager.getJedisCluster().hmset(key, JsonUtil.object2Map(role));
	}
	
	
	
	
}
