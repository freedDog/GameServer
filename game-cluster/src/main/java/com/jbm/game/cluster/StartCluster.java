package com.jbm.game.cluster;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.cluster.server.ClusterServer;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.redis.jedis.JedisClusterConfig;
import com.jbm.game.engine.thread.ThreadPoolExecutorConfig;
import com.jbm.game.engine.util.FileUtil;
import com.jbm.game.engine.util.SysUtil;

/**
 * 集群启动类
 * <p>
 * 服务器注册管理中心
 * <br> 是否可用zookeeper替换
 * @author JiangBangMing
 *
 * 2018年7月24日 下午12:37:57
 */
public class StartCluster {
	private static final Logger logger=LoggerFactory.getLogger(StartCluster.class);
	
	private static ClusterServer clusterServer;
	public static String path="";
	
	public static void main(String[] args) {
		File file=new File(System.getProperty("user.dir"));
		if("target".equals(file.getName())) {
			path=file.getPath()+File.separatorChar+"config";
		}else {
			path=file.getPath()+File.separatorChar+"target"+File.separatorChar+"config";
		}
		logger.info("配置路径为:"+path);
		JedisClusterConfig jedisClusterConfig=FileUtil.getConfigXML(path, "jedisclusterConfig.xml", JedisClusterConfig.class);
		if(jedisClusterConfig==null) {
			SysUtil.exit(StartCluster.class,null,"jedisclusterConfig");
		}
		ThreadPoolExecutorConfig threadPoolExecutorConfig_http=FileUtil.getConfigXML(path, "threadExcutorConfig_http.xml", ThreadPoolExecutorConfig.class);
		if(threadPoolExecutorConfig_http==null) {
			SysUtil.exit(StartCluster.class, null, "threadPoolExecutorConfig_http");
		}
		ThreadPoolExecutorConfig threadPoolExecutorConfig_tcp=FileUtil.getConfigXML(path, "threadExcutorConfig_tcp.xml",ThreadPoolExecutorConfig.class);
		if(threadPoolExecutorConfig_tcp==null) {
			SysUtil.exit(StartCluster.class, null, "threadPoolExecutorConfig_tcp");
		}
		
		MinaServerConfig minaServerConfig_http=FileUtil.getConfigXML(path, "minaServerConfig_http.xml",MinaServerConfig.class);
		if(minaServerConfig_http==null) {
			SysUtil.exit(StartCluster.class, null, "minaServerConfig_http");
		}
		MinaServerConfig minaServerConfig_tcp=FileUtil.getConfigXML(path, "minaServerConfig_tcp.xml", MinaServerConfig.class);
		if(minaServerConfig_tcp==null) {
			SysUtil.exit(StartCluster.class, null,"minaServerConfig_tcp");
		}
		
		clusterServer=new ClusterServer(threadPoolExecutorConfig_http, minaServerConfig_http, threadPoolExecutorConfig_tcp, minaServerConfig_tcp);
		new Thread(clusterServer).start();
	}
	
	public static ClusterServer getClusterServer() {
		return clusterServer;
	}
	
}
