package com.jbm.game.model.script;

import com.jbm.game.engine.script.IScript;
import com.jbm.game.message.ServerMessage.ServerInfo;

/**
 * 游戏服务器状态监测脚本
 * @author JiangBangMing
 *
 * 2018年7月23日 下午2:39:23
 */
public interface IGameServerCheckScript extends IScript{

	
	/**
	 * 构建服务器状态信息
	 * @param builder
	 */
	public default void buildServerInfo(ServerInfo.Builder builder) {
		
	}
}
