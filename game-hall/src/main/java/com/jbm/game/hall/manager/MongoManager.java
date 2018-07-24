package com.jbm.game.hall.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.mongo.AbsMongoManager;

/**
 * mongodb
 * @author JiangBangMing
 *
 * 2018年7月24日 下午5:31:57
 */
public class MongoManager extends AbsMongoManager{

	private static final Logger logger=LoggerFactory.getLogger(MongoManager.class);
	private static final MongoManager INSTANCE_MANAGER=new MongoManager();
	private MongoManager() {
		
	}
	
	public static final MongoManager getInstance() {
		return INSTANCE_MANAGER;
	}
	
	@Override
	protected void initDao() {
		
	}
	
}
