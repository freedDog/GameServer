package com.jbm.game.bydr;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.manager.MongoManager;
import com.jbm.game.bydr.server.BydrServer;
import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.script.ScriptManager;

public class StartBydr {
	
	private static final Logger logger=LoggerFactory.getLogger(StartBydr.class);
	
	private static String configPath;
	private static JedisManager redisManager;
	private static BydrServer bydrServer;
	
	public static void main(String[] args) {
		initConfigPath();
		//redis
		redisManager=new JedisManager(configPath);
		redisManager.initScript(configPath);
		JedisManager.setReadisManager(redisManager);
		
		//创建mongodb 连接
		MongoManager.getInstance().createConnect(configPath);
		
		//加载脚本
		ScriptManager.getInstance().init( str ->System.exit(0));
		
		//启动通信连接
		bydrServer=new BydrServer(configPath);
		new Thread(bydrServer).start();
	}
	
	private static void initConfigPath() {
		File file=new File(System.getProperty("user.dir"));
		if("target".equals(file.getName())) {
			configPath=file.getPath()+File.separatorChar+"config";
		}else {
			configPath=file.getPath()+File.separatorChar+"target"+File.separatorChar+"config";
		}
		logger.info("配置路劲为:"+configPath);
	}

	public static String getConfigPath() {
		return configPath;
	}

	public static BydrServer getBydrServer() {
		return bydrServer;
	}
	
	
	
}
