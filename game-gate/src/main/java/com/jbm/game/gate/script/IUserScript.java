package com.jbm.game.gate.script;

import org.apache.mina.core.session.IoSession;

import com.jbm.game.engine.script.IScript;
import com.jbm.game.model.constant.Reason;

/**
 * 用户接口
 * @author JiangBangMing
 *
 * 2018年7月21日 下午12:31:22
 */
public interface IUserScript extends IScript{

	/**
	 * 用户退出处理
	 * @param session 游戏客户端会话
	 */
	public default void quit(IoSession session,Reason reason) {
		
	}
}
