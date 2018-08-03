package com.jbm.game.model.redis.channel;

public enum BydrWorldChannel {
	/**报名参加竞技赛*/
	ApplyAthleticsReq;
	
	public static String[] getChannels() {
		BydrWorldChannel[] values = BydrWorldChannel.values();
		String[] channels = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			channels[i] = values[i].name();
		}
		return channels;
	}
}
