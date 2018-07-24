package com.jbm.game.hall.script;

import java.util.function.Consumer;

import com.jbm.game.engine.script.IScript;
import com.jbm.game.model.struct.User;

/**
 * 用户接口
 * @author JiangBangMing
 *
 * 2018年7月23日 下午3:54:06
 */
public interface IUserScript extends IScript{

	
	/**
	 * 创建用户
	 * @param userConsumer
	 * @return
	 */
	public default User createUser(Consumer<User> userConsumer) {
		return null;
	}
}
