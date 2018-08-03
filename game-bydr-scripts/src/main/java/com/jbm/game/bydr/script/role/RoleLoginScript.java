package com.jbm.game.bydr.script.role;

import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.manager.RoleManager;
import com.jbm.game.bydr.script.IRoleScript;
import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.util.JsonUtil;
import com.jbm.game.model.constant.Config;
import com.jbm.game.model.constant.Reason;
import com.jbm.game.model.redis.key.HallKey;

/**
 * 登录
 * @author JiangBangMing
 *
 * 2018年8月3日 下午2:17:52
 */
public class RoleLoginScript implements IRoleScript{

	private static final Logger logger=LoggerFactory.getLogger(RoleLoginScript.class);
	
	@Override
	public void login(long roleId, Reason reason, Consumer<Role> roleConsumer) {
		Role role=RoleManager.getInstance().loadRoleData(roleId);
		if(role==null) {
			role=new Role();
			role.setId(roleId);
		}
		
		role.setGameId(Config.SERVER_ID);
		if(roleConsumer!=null) {
			roleConsumer.accept(role);
		}
		
		RoleManager.getInstance().getOnlineRoles().put(roleId, role);
		//同步大厅角色数据，昵称，头像等
		syncHallData(role);
	}
	
	/**
	 * 同步大厅数据
	 * @param role
	 */
	private void syncHallData(Role role) {
		//同步角色
		String key=HallKey.Role_Map_Info.getKey(role.getId());
		Map<String, String> hgetAll=JedisManager.getJedisCluster().hgetAll(key);
		if(hgetAll==null) {
			logger.warn("{}未找到角色数据",key);
			return;
		}
		com.jbm.game.model.struct.Role hallRole=new com.jbm.game.model.struct.Role();
		JsonUtil.map2Object(hgetAll, hallRole);
		role.setNick(hallRole.getNick());
		role.setGold(hallRole.getGold());
		role.setLeaveTime(hallRole.getLevel());
		RoleManager.getInstance().saveRoleData(role);
	}
}
