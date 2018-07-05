package com.jbm.game.engine.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.thread.ThreadType;

/**
 * 抽象服务
 * @author JiangBangMing
 *
 * 2018年7月5日 下午9:06:52
 */
public abstract class Service <Conf extends BaseServerConfig> implements Runnable{
	
	private static final Logger logger=LoggerFactory.getLogger(Service.class);
	
	private final Map<ThreadType, Executor> serverThreads=new ConcurrentHashMap<ThreadType, Executor>();
	
	
	
	
	public <T extends Executor> T getExecutor(ThreadType threadType) {
		return (T)serverThreads.get(threadType);
	}
}
		
