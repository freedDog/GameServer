package com.jbm.game.hall.script.role;

import com.jbm.game.hall.manager.RoleManager;
import com.jbm.game.hall.script.IRoleScript;
import com.jbm.game.model.constant.Reason;
import com.jbm.game.model.struct.Role;

/**
 * 角色退出游戏
 * @author JiangBangMing
 *
 * 2018年7月25日 下午5:45:22
 */
public class RoleQuitScript implements IRoleScript{

	@Override
	public void quit(Role role, Reason reason) {
//		RoleManager.getInstance().getRoles().remove(role.getId());
		saveData(role);
	}
	
	private void saveData(Role role) {
		//-------------mongodb----------------
//		RoleDao.saveRole(role);
		
		//角色 数据不保存，需要实时存储
//		String key = HallKey.Role_Map_Info.getKey(role.getId());
//		Map<String, String> map = JsonUtil.object2Map(role);
//		JedisManager.getJedisCluster().hmset(key, map);
	}
}
