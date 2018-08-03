package com.jbm.game.bydr.script.role;

import com.jbm.game.bydr.manager.RoleManager;
import com.jbm.game.bydr.script.IRoleScript;
import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.model.constant.Reason;

/**
 * 退出
 * @author JiangBangMing
 *
 * 2018年8月3日 下午2:25:14
 */
public class RoleQuitScript implements IRoleScript{
	@Override
	public void quit(Role role, Reason reason) {
		RoleManager roleManager=RoleManager.getInstance();
		
		role.setGameId(0);
		roleManager.getOnlineRoles().remove(role.getId());
		role.syncGold(Reason.UserQuit);
		roleManager.saveRoleData(role);
	}
}
