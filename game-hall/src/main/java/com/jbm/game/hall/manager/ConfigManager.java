package com.jbm.game.hall.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.model.mongo.bydr.entity.CFish;

/**
 * 配置表
 * @author JiangBangMing
 *
 * 2018年7月24日 下午1:33:00
 */
public class ConfigManager {

	private static final Logger logger=LoggerFactory.getLogger(ConfigManager.class);
	public static volatile ConfigManager configManager;
	/**配置信息*/
	public Map<Integer, CFish> fishMap=new ConcurrentHashMap<>();
	
	private ConfigManager() {
		
	}
	
	public static ConfigManager getInstance() {
		if(configManager==null) {
			synchronized (ConfigManager.class) {
				if(configManager==null) {
					configManager=new ConfigManager();
				}
			}
		}
		return configManager;
	}

	/**
	 * 获取配置信息
	 * @param configId
	 * @return
	 */
	public CFish getFish(int configId) {
		if(fishMap.containsKey(configId)) {
			return fishMap.get(configId);
		}
		logger.warn("CFish配置错误:{} 未配置",configId);
		return null;
	}
	
	public Map<Integer, CFish> getFishMap() {
		return fishMap;
	}

	public void setFishMap(Map<Integer, CFish> fishMap) {
		this.fishMap = fishMap;
	}
	
	
}
