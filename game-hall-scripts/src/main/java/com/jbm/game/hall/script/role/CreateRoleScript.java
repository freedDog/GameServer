package com.jbm.game.hall.script.role;

import java.util.function.Consumer;

import com.jbm.game.hall.script.IRoleScript;
import com.jbm.game.model.struct.Role;

/**
 * 创建角色
 * @author JiangBangMing
 *
 * 2018年7月25日 下午5:39:37
 */
public class CreateRoleScript implements IRoleScript{

	@Override
	public Role createRole(long userId, Consumer<Role> roleConsumer) {
		Role role=new Role();
//		role.setId(HallInfoDao.getRoleId);
		if(roleConsumer!=null) {
			roleConsumer.accept(role);
		}
		return role;
	}
	
}
