package com.jbm.game.model.redis.key;

/**
 * 大厅redis 数据key 枚举
 * @author JiangBangMing
 *
 * 2018年7月25日 上午10:53:05
 */
public enum HallKey {
	/** 服务器启动时间 */
	GM_Hall_StartTime("GM_%d:Hall:starttime"),

	/** 角色基本信息 */
	Role_Map_Info("Role_%d:Map:info"),
	/**角色背包*/
	Role_Map_Packet("Role_%d:Map:packet"),
	;

	private final String key;

	private HallKey(String key) {
		this.key = key;
	}

	public String getKey(Object... objects) {
		return String.format(key, objects);
	}
}
