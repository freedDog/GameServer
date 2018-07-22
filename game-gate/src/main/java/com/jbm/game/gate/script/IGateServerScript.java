package com.jbm.game.gate.script;

import org.apache.mina.filter.firewall.BlacklistFilter;

import com.jbm.game.engine.script.IScript;
import com.jbm.game.engine.server.ServerType;

/**
 * 服务器脚本
 * @author JiangBangMing
 *
 * 2018年7月21日 下午12:28:13
 */
public interface IGateServerScript extends IScript{

	/**
	 * 是否为udp消息
	 * @param serverType 判断游戏类型是否支持udp
	 * @param msgId 消息ID
	 * @return
	 */
	public default boolean isUdpMsg(ServerType serverType,int msgId) {
		return false;
	}
	
	/**
	 * 设置IP黑名单
	 * @param filter
	 */
	public default void setIpBlackList(BlacklistFilter filter) {
		
	}
}
