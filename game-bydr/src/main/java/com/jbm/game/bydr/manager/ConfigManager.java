package com.jbm.game.bydr.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.StartBydr;
import com.jbm.game.bydr.config.GameConfig;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.util.FileUtil;
import com.jbm.game.model.mongo.bydr.entity.CFish;
import com.jbm.game.model.script.IConfigScript;

/**
 * 配置管理
 * @author JiangBangMing
 *
 * 2018年8月2日 下午4:54:04
 */
public class ConfigManager {

	private static final Logger logger=LoggerFactory.getLogger(ConfigManager.class);
	private static volatile ConfigManager configManager=null;
	private GameConfig gameConfig=new GameConfig();
	
	//配置信息
	private Map<Integer, CFish> fishMap=new ConcurrentHashMap<>();
	
	private ConfigManager() {
		super();
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
	 * 加载配置表
	 */
	public void loadConfig() {
		gameConfig=FileUtil.getConfigXML(StartBydr.getConfigPath(),"gameConfig.xml", GameConfig.class);
		if(gameConfig==null) {
			throw new RuntimeException(String.format("游戏常量%s /gameConfig.xml 文件不存在", StartBydr.getConfigPath()));
		}
		ScriptManager.getInstance().getBaseScriptEntry().functionScripts(IConfigScript.class, (IConfigScript script) -> script.reloadConfig(null));
	}
	
	
	public Map<Integer, CFish> getFishMap() {
		return fishMap;
	}

	public void setFishMap(Map<Integer, CFish> fishMap) {
		this.fishMap = fishMap;
	}

	public GameConfig getGameConfig() {
		return gameConfig;
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
		logger.warn("CFish 配置错误:{} 未配置",configId);
		return null;
	}
}
