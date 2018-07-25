package com.jbm.game.hall.script;

import java.util.function.Consumer;

import com.jbm.game.engine.script.IScript;
import com.jbm.game.model.constant.Reason;
import com.jbm.game.model.struct.Role;

/**
 * 玩家脚本
 * @author JiangBangMing
 *
 * 2018年7月23日 下午3:55:41
 */
public interface IRoleScript extends IScript{

	/**
	 * 登录游戏
	 * @param role
	 */
	public default void login(Role role,Reason reason) {
		
	}
	/**
	 * 创建角色
	 * @param userId
	 * @param roleConsumer
	 * @return
	 */
	public default Role createRole(long userId,Consumer<Role> roleConsumer) {
		return null;
	}
	
	/**
	 * 角色退出游戏
	 * @param role
	 * @param reason
	 */
	public default void quit(Role role,Reason reason) {
		
	}
	
}
