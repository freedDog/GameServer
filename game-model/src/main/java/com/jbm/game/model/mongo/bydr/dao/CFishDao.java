package com.jbm.game.model.mongo.bydr.dao;

import java.util.List;
import org.mongodb.morphia.dao.BasicDAO;
import com.jbm.game.engine.mongo.AbsMongoManager;
import com.jbm.game.model.mongo.bydr.entity.CFish;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年7月23日 下午3:35:49
 */
public class CFishDao extends BasicDAO<CFish,Integer>{

	private static volatile CFishDao cFishDao=null;
	
	public CFishDao(AbsMongoManager mongoManager) {
		super(CFish.class, mongoManager.getMongoClient(),mongoManager.getMorphia(),
				mongoManager.getMongoClientConfig().getDbName());
	}
	
	public static CFishDao getDB(AbsMongoManager mongoManager) {
		if(cFishDao==null) {
			synchronized (CFishDao.class) {
				if(cFishDao==null) {
					cFishDao=new CFishDao(mongoManager);
				}
			}
		}
		return cFishDao;
	}
	
	public static List<CFish> getAll(){
		return cFishDao.createQuery().asList();
	}

}
