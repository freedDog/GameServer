package com.jbm.game.hall.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.mina.config.MinaClientConfig;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.mq.MQConsumer;
import com.jbm.game.engine.redis.jedis.JedisPubListener;
import com.jbm.game.engine.server.ServerState;
import com.jbm.game.engine.thread.ThreadPoolExecutorConfig;
import com.jbm.game.engine.util.FileUtil;
import com.jbm.game.message.ServerMessage;
import com.jbm.game.message.ServerMessage.ServerRegisterRequest;
import com.jbm.game.model.constant.Config;
import com.jbm.game.model.redis.channel.HallChannel;
import com.jbm.game.model.timer.GameServerCheckTimer;

/**
 * 大厅服务器
 * @author JiangBangMing
 *
 * 2018年7月24日 下午3:34:19
 */
public class HallServer implements Runnable{

	private static final Logger logger=LoggerFactory.getLogger(HallServer.class);
	
	//连接网关( 接受网关转发过来的消息)
	private Hall2GateClient hall2GateClient;
	
	//连接集群服(获取各服务器信息)
	private Hall2ClusterClient hall2ClusterClient;
	
	//htt服务
	private HallHttpServer hallHttpServer;
	
	//redis 订阅发布
	private final JedisPubListener hallPubListener;
	
	//服务器状态监测
	private GameServerCheckTimer hallServerCheckTimer;
	//MQ消息
	private MQConsumer mqConsumer;
	
	public HallServer(String configPath) {
		
		//加载连接大厅客户端配置
		ThreadPoolExecutorConfig hallClientThreadPool=FileUtil.getConfigXML(configPath, "hallClientThreadPoolExecutorConfig.xml", ThreadPoolExecutorConfig.class);
		if(hallClientThreadPool==null) {
			logger.error("{} /hallClientThreadPoolExecutorConfig.xml 未找到",configPath);
			System.exit(0);
		}
		MinaClientConfig minaClientConfig_gate=FileUtil.getConfigXML(configPath, "minaClientConfig_gate.xml", MinaClientConfig.class);
		if(minaClientConfig_gate==null) {
			logger.error("{}/minaClientConfig_gate.xml 未找打",configPath);
			System.exit(0);
		}
		
		//加载连接集群配置
		MinaClientConfig minaClientConfig_cluster=FileUtil.getConfigXML(configPath, "minaClientConfig_cluster.xml", MinaClientConfig.class);
		if(minaClientConfig_cluster==null) {
			logger.error("{}/minaClientConfig_cluster.xml 未找到",configPath);
			System.exit(0);
		}
		//http配置
		MinaServerConfig minaServerConfig_http=FileUtil.getConfigXML(configPath, "minaServerConfig_http.xml", MinaServerConfig.class);
		if(minaServerConfig_http==null) {
			logger.error("{}/minaServerConfig_http.xml 未找到",configPath);
			System.exit(0);
		}
		
		this.hall2GateClient=new Hall2GateClient(hallClientThreadPool, minaClientConfig_gate);
		this.hall2ClusterClient=new Hall2ClusterClient(minaClientConfig_cluster);
		
		this.hallServerCheckTimer=new GameServerCheckTimer(hall2ClusterClient, hall2GateClient, minaClientConfig_gate);
		
		hallPubListener=new JedisPubListener(HallChannel.getChannels());
		
		mqConsumer=new MQConsumer(configPath,"hall");
		Config.SERVER_ID=minaClientConfig_gate.getId();
	}
	
	@Override
	public void run() {
		new Thread(this.hall2GateClient).start();
		new Thread(this.hall2ClusterClient).start();
		new Thread(this.hallHttpServer).start();
		this.hallServerCheckTimer.start();
		this.hallPubListener.start();
	}
	
	/**
	 * 构建服务器更新注册信息
	 * @param minaClientConfig
	 * @return
	 */
	public ServerRegisterRequest buildServerRegisterRequest(MinaClientConfig minaClientConfig) {
		ServerRegisterRequest.Builder builder=ServerRegisterRequest.newBuilder();
		ServerMessage.ServerInfo.Builder info=ServerMessage.ServerInfo.newBuilder();
		info.setId(minaClientConfig.getId());
		info.setIp("");
		info.setMaxUserCount(1000);
		info.setOnline(1);
		info.setName(minaClientConfig.getName());
		info.setState(ServerState.NORMAL.getState());
		info.setTotalMemory(minaClientConfig.getType().getType());
		info.setWwwip("");
		builder.setServerInfo(info);
		return builder.build();
		
	}
	
	public Hall2GateClient getHall2GateClient() {
		return hall2GateClient;
	}

	public Hall2ClusterClient getHall2ClusterClient() {
		return hall2ClusterClient;
	}

	public HallHttpServer getHallHttpServer() {
		return hallHttpServer;
	}
	
	
}
