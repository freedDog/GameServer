package com.jbm.game.bydr.manager;

import com.jbm.game.engine.mongo.AbsMongoManager;

/**
 * mongodb
 * @author JiangBangMing
 *
 * 2018年8月2日 下午5:04:26
 */
public class MongoManager extends AbsMongoManager{

	private static final  MongoManager mongoManager=new MongoManager();
	
	public static final  MongoManager getInstance(){
		return mongoManager;
	}
	
	@Override
	protected void initDao() {
		
	}

}
