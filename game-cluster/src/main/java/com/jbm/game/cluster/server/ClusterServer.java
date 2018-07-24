package com.jbm.game.cluster.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.cluster.StartCluster;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.thread.ThreadPoolExecutorConfig;
import com.jbm.game.engine.util.SysUtil;
import com.jbm.game.model.constant.NetPort;

/**
 * 集群管理服务器
 * @author JiangBangMing
 *
 * 2018年7月24日 下午12:27:18
 */
public class ClusterServer  implements Runnable{

	private static final Logger logger=LoggerFactory.getLogger(ClusterServer.class);
	
	private final ClusterTcpServer clusterTcpServer;
	private final ClusterHttpServer clusterHttpServer;
	
	public ClusterServer(ThreadPoolExecutorConfig defalutThreadExecutorConfig4HttpServer,MinaServerConfig minaServerConfig4HttpServer
			,ThreadPoolExecutorConfig defaultThreadExcutorConfig4TcpServer,MinaServerConfig minaServerConfig4TcpConfig) {
		NetPort.CLUSTER_PORT=minaServerConfig4TcpConfig.getPort();
		NetPort.CLUSTER_HTTP_PORT=minaServerConfig4HttpServer.getHttpPort();
		clusterHttpServer=new ClusterHttpServer(defalutThreadExecutorConfig4HttpServer, minaServerConfig4HttpServer);
		clusterTcpServer=new ClusterTcpServer(defaultThreadExcutorConfig4TcpServer, minaServerConfig4TcpConfig);
	}
	
	@Override
	public void run() {
		logger.info("ClusterServer::clusterHttpServer::start!");
		new Thread(clusterHttpServer).start();
		logger.info("ClusterServer::clusterTcpServer::start!");
		new Thread(clusterTcpServer).start();
		
//		ScriptManager.getInstance().init((str) -> SysUtil.exit(this.getClass(), null, "加载脚本错误"));
		ScriptManager.getInstance().init((str) -> System.out.println("脚本加载完成"));
		
		try {
			Thread.sleep(1000);
		}catch (InterruptedException e) {
			logger.error("",e);
		}
		logger.info("------>集群服启动成功<------");
	}
	
	public static ClusterServer getInstance() {
		return StartCluster.getClusterServer();
	}
	
	public ClusterHttpServer getLoginHttpServer() {
		return clusterHttpServer;
	}
	
	public ClusterTcpServer getLoginTcpServer() {
		return clusterTcpServer;
	}
	
}
