package com.jbm.game.hall.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 公会管理
 * @author JiangBangMing
 *
 * 2018年7月25日 下午3:38:54
 */
public class GuildManager {

	private static final Logger logger=LoggerFactory.getLogger(GuildManager.class);
	private static volatile GuildManager guildManager;
	
	private GuildManager() {
		
	}
	
	public static GuildManager getInstance() {
		if(guildManager==null) {
			synchronized (GuildManager.class) {
				if(guildManager==null) {
					guildManager=new GuildManager();
				}
			}
		}
		return guildManager;
	}
	
}
