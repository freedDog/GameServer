package com.jbm.game.hall;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.hall.manager.MongoManager;
import com.jbm.game.hall.server.HallServer;

/**
 * 大厅启动类
 * @author JiangBangMing
 *
 * 2018年7月24日 下午5:26:21
 */
public class StartHall {

	private static final Logger logger=LoggerFactory.getLogger(StartHall.class);
	private static String configPath;
	protected static JedisManager jedisManager;
	private static HallServer bydrServer;
	
	public static void main(String[] args) {
		initConfigPath();
		
		jedisManager=new JedisManager(configPath);
		
		//创建mongodb连接
		MongoManager.getInstance().createConnect(configPath);
		
		//加载脚本
//		ScriptManager.getInstance().init(str -> System.exit(0));
		ScriptManager.getInstance().init(str -> System.out.println("加载脚本完成"));
		
		//启动通信链接
		bydrServer=new HallServer(configPath);
		new Thread(bydrServer).start();
	}
	
	public static void initConfigPath() {
		File file=new File(System.getProperty("user.dir"));
		if("target".equals(file.getName())) {
			configPath=file.getPath()+File.separatorChar+"config";
		}else {
			configPath=file.getPath()+File.separatorChar+"target"+File.separatorChar+"config";
		}
		logger.info("配置路劲为:"+configPath);
	}
	
	public static HallServer getBydrServer() {
		return bydrServer;
	}
}
