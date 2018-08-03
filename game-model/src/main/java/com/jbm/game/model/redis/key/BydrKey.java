package com.jbm.game.model.redis.key;

/**
 * redis 数据key 枚举
 * @author JiangBangMing
 *
 * 2018年8月2日 下午1:47:48
 */
public enum BydrKey {
	/** 角色基本信息 */
	Team_Map("Bydr:Team:Map"),
	/**角色信息*/
	Role_Map("Bydr:Role_%d:Map")
	;

	private final String key;

	private BydrKey(String key) {
		this.key = key;
	}

	public String getKey(Object... objects) {
		return String.format(key, objects);
	}
}
