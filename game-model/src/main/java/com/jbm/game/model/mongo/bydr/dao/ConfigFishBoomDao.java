/**工具生成，请遵循规范。
 @author JiangBangMing
*/
package com.jbm.game.model.mongo.bydr.dao;

import java.util.List;
import org.mongodb.morphia.dao.BasicDAO;
import com.jbm.game.engine.mongo.AbsMongoManager;
import com.jbm.game.model.mongo.bydr.entity.ConfigFishBoom;

public class ConfigFishBoomDao extends BasicDAO<ConfigFishBoom, Integer> {
	private static volatile ConfigFishBoomDao configFishBoomDao = null;

	public ConfigFishBoomDao(AbsMongoManager mongoManager) {
		super(ConfigFishBoom.class, mongoManager.getMongoClient(), mongoManager.getMorphia(),mongoManager.getMongoClientConfig().getDbName());
	}

	public static ConfigFishBoomDao getDB(AbsMongoManager mongoManager) {
		if(configFishBoomDao == null) {
			synchronized (ConfigFishBoomDao.class){
				if(configFishBoomDao == null){
					configFishBoomDao = new ConfigFishBoomDao(mongoManager);
					}
				}
			}
		return configFishBoomDao;
	}

	public static List<ConfigFishBoom> getAll(){
		 return configFishBoomDao.createQuery().asList();
	}

}