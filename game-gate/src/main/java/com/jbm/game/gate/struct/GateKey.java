package com.jbm.game.gate.struct;

/**
 * 网关 redis key 枚举
 * @author JiangBangMing
 *
 * 2018年7月22日 下午3:32:22
 */
public enum GateKey {
	/** 服务器启动时间 */
	GM_Gate_StartTime("GM_%d:Hall:starttime"),
	;
	
	private final String key;

	private GateKey(String key) {
		this.key = key;
	}

	public String getKey(Object... objects) {
		return String.format(key, objects);
	}
	
}
