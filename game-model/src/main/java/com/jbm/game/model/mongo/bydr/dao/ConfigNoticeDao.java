/**工具生成，请遵循规范。
 @author JiangBangMing
*/
package com.jbm.game.model.mongo.bydr.dao;

import java.util.List;
import org.mongodb.morphia.dao.BasicDAO;
import com.jbm.game.engine.mongo.AbsMongoManager;
import com.jbm.game.model.mongo.bydr.entity.ConfigNotice;

public class ConfigNoticeDao extends BasicDAO<ConfigNotice, Integer> {
	private static volatile ConfigNoticeDao configNoticeDao = null;

	public ConfigNoticeDao(AbsMongoManager mongoManager) {
		super(ConfigNotice.class, mongoManager.getMongoClient(), mongoManager.getMorphia(),mongoManager.getMongoClientConfig().getDbName());
	}

	public static ConfigNoticeDao getDB(AbsMongoManager mongoManager) {
		if(configNoticeDao == null) {
			synchronized (ConfigNoticeDao.class){
				if(configNoticeDao == null){
					configNoticeDao = new ConfigNoticeDao(mongoManager);
					}
				}
			}
		return configNoticeDao;
	}

	public static List<ConfigNotice> getAll(){
		 return configNoticeDao.createQuery().asList();
	}

}