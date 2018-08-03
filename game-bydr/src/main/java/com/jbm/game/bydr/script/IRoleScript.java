package com.jbm.game.bydr.script;

import java.util.function.Consumer;

import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.engine.script.IScript;
import com.jbm.game.model.constant.Reason;

/**
 * 角色
 * @author JiangBangMing
 *
 * 2018年8月2日 下午1:31:04
 */
public interface IRoleScript extends IScript{

	/**
	 * 登录
	 * @param roleId
	 * @param reason
	 * @param roleConsumer
	 */
	public default void login(long roleId,Reason reason,Consumer<Role> roleConsumer) {
		
	}
	
	/**
	 * 退出
	 * @param role
	 * @param reason
	 */
	public default void quit(Role role,Reason reason) {
		
	}
	
	/**
	 *修改金币
	 * @param role
	 * @param gold
	 * @param reason
	 */
	public default void changeGold(Role role,int gold,Reason reason) {
		
	}
	
	/**
	 * 将金币同步到大厅
	 * @param role
	 * @param reason
	 */
	public default void syncGold(Role role,Reason reason) {
		
	}
}
