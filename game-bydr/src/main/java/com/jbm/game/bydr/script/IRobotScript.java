package com.jbm.game.bydr.script;

import java.util.function.Consumer;

import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.engine.script.IScript;

/**
 * 机器人脚本
 * @author JiangBangMing
 *
 * 2018年8月2日 下午1:28:50
 */
public interface IRobotScript extends IScript{

	/**
	 * 创建机器人
	 * @param roleConsumer
	 * @return
	 */
	public default Role createRobot(Consumer<Role> roleConsumer) {
		return null;
	}
	
	/**
	 * 检查机器人金币
	 * @param robot
	 */
	public default void checkGold(Role robot) {
		
	}
}
