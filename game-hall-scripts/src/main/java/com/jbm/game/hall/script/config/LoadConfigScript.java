package com.jbm.game.hall.script.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.hall.manager.ConfigManager;
import com.jbm.game.model.mongo.bydr.dao.CFishDao;
import com.jbm.game.model.mongo.bydr.entity.CFish;
import com.jbm.game.model.script.IConfigScript;

/**
 * 加载配置脚本
 * @author JiangBangMing
 *
 * 2018年7月25日 下午3:03:32
 */
public class LoadConfigScript implements IConfigScript{

	
	private static final Logger logger=LoggerFactory.getLogger(LoadConfigScript.class);
	
	@Override
	public String reloadConfig(List<String> tables) {
		StringBuffer sb=new StringBuffer();
		synchronized (this) {
			try {
				if(containTable(tables, CFish.class)) {
					Map<Integer, CFish> fishMap=new ConcurrentHashMap<>();
					CFishDao.getAll().forEach(fish ->{
						fishMap.put(fish.getId(), fish);
					});
					ConfigManager.getInstance().setFishMap(fishMap);
					sb.append("CFish:").append(fishMap.size());
				}
				
				//TODO 其他配置
			}catch (Exception e) {
				logger.error("加载配置",e);
			}
		}
		return sb.toString();
	}
}
