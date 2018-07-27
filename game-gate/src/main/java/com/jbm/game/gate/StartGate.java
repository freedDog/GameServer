package com.jbm.game.gate;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.redis.jedis.JedisClusterConfig;
import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.util.FileUtil;
import com.jbm.game.gate.manager.MongoManager;
import com.jbm.game.gate.server.GateServer;

/**
 * 网关启动类
 * @author JiangBangMing
 *
 * 2018年7月22日 下午3:06:05
 */
public class StartGate {
	
	private static final Logger logger=LoggerFactory.getLogger(StartGate.class);
	
	private static String configPath;
	private static JedisManager jedisManager;
	private static GateServer gateServer;
	
	public static void main(String[] args) {
		initConfigPath();
		
		//redis
		jedisManager=new JedisManager(configPath);
		//创建mongodb连接
		MongoManager.getInstance().createConnect(configPath);
		//加载脚本
		ScriptManager.getInstance().init(null);
		//通信服务
		gateServer=new GateServer();
		new Thread(gateServer).start();
	}
	
	
	public static String getConfigPath() {
		return configPath;
	}


	public static GateServer getGateServer() {
		return gateServer;
	}


	private static void initConfigPath() {
		File file=new File(System.getProperty("user.dir"));
		if("target".equals(file.getName())) {
			configPath=file.getPath()+File.separatorChar+"config";
		}else {
			configPath=file.getPath()+File.separatorChar+"target"+File.separatorChar+"config";
		}
		logger.info("配置路径为:"+configPath);
	}

}
