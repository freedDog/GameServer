package com.jbm.game.model.redis.channel;

/**
 * 大厅订阅发布 通道
 * @author JiangBangMing
 *
 * 2018年7月24日 下午4:51:16
 */
public enum HallChannel {

	/**登录大厅*/
	LoginHall,
	/**大厅金币更新*/
	HallGoldChange,
	;
	public static String[] getChannels() {
		HallChannel[] values = HallChannel.values();
		String[] channels = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			channels[i] = values[i].name();
		}
		return channels;
	}
}
