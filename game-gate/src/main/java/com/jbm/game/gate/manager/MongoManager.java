package com.jbm.game.gate.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.mongo.AbsMongoManager;

/**
 * mongodb
 * @author JiangBangMing
 *
 * 2018年7月19日 下午3:17:41
 */
public class MongoManager extends AbsMongoManager{
	
	private static final Logger logger=LoggerFactory.getLogger(MongoManager.class);
	private static final MongoManager mongoManager=new MongoManager();
	
	private MongoManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static final MongoManager getInstance() {
		return mongoManager;
	}
	

	@Override
	protected void initDao() {
		// TODO Auto-generated method stub
		
	}

}
