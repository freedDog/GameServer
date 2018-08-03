package com.jbm.game.bydr.script.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.bydr.manager.ConfigManager;
import com.jbm.game.model.mongo.bydr.dao.CFishDao;
import com.jbm.game.model.mongo.bydr.entity.CFish;
import com.jbm.game.model.script.IConfigScript;

/**
 * 加载配置脚本
 * @author JiangBangMing
 *
 * 2018年8月3日 下午1:39:28
 */
public class LoadConfigScript implements IConfigScript{

	private static final Logger logger=LoggerFactory.getLogger(LoadConfigScript.class);
	
	
	@Override
	public String reloadConfig(List<String> tableName) {
		StringBuffer sb=new StringBuffer();
		synchronized (this) {
			try {
				if(containTable(tableName, CFish.class)) {
					Map<Integer, CFish> fishMap=new ConcurrentHashMap<>();
					CFishDao.getAll().forEach( fish ->{
						fishMap.put(fish.getId(), fish);
					});
					
					ConfigManager.getInstance().setFishMap(fishMap);
					sb.append("SFish:").append(fishMap.size());
				}
			}catch (Exception e) {
				logger.error("加载配置",e);
			}
		}
		logger.info(sb.toString());
		return sb.toString();
	}
}
