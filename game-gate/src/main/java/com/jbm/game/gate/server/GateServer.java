package com.jbm.game.gate.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.engine.mina.config.MinaClientConfig;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.server.ServerState;
import com.jbm.game.engine.thread.ThreadPoolExecutorConfig;
import com.jbm.game.engine.util.FileUtil;
import com.jbm.game.engine.util.SysUtil;
import com.jbm.game.gate.StartGate;
import com.jbm.game.gate.manager.UserSessionManager;
import com.jbm.game.gate.server.client.Gate2ClusterClient;
import com.jbm.game.gate.struct.Config;
import com.jbm.game.gate.struct.GateKey;
import com.jbm.game.message.ServerMessage.ServerRegisterRequest;

/**
 * 大厅服务器
 * @author JiangBangMing
 *
 * 2018年7月22日 下午3:00:55
 */
public class GateServer implements Runnable{

	private static final Logger logger=LoggerFactory.getLogger(GateServer.class);
	
	private final GateTcpUserServer gateTcpUserServer;//用户tcp
	private final Gate2ClusterClient clusterClient;//连接集群服
	private final GateTcpGameServer gateTcpGameServer;//内部转发tcp
	private GateUdpUserServer gateUdpUserServer;//用户udp;
	private GateWebSocketUserServer gateWebSocketUserServer;//用户websocket
	private GateHttpServer gateHttpServer;//http 通信
	
	public GateServer() {
		
		//线程池
		ThreadPoolExecutorConfig threadPoolExecutorConfig=FileUtil.getConfigXML(StartGate.getConfigPath(), 
				"threadPoolExecutorConfig.xml",ThreadPoolExecutorConfig.class);
		if(threadPoolExecutorConfig==null) {
			logger.error("线程池配置不存在");
			System.exit(1);
		}
		
		// 用户Tcp服务器
		MinaServerConfig minaServerConfig_user=FileUtil.getConfigXML(StartGate.getConfigPath(), 
				"minaServerConfig_user.xml",MinaServerConfig.class);
		if(minaServerConfig_user==null) {
			logger.error("mina 服务器 配置不存在");
			System.exit(1);
		}
		gateTcpUserServer=new GateTcpUserServer(threadPoolExecutorConfig, minaServerConfig_user);
		
		//游戏tcp 服务器
		MinaServerConfig minaServerConfig_game=FileUtil.getConfigXML(StartGate.getConfigPath(),
				"minaServerConfig_game.xml", MinaServerConfig.class);
		if(minaServerConfig_game==null) {
			logger.error("mina服务器配置不存在");
			System.exit(1);
		}
		gateTcpGameServer=new GateTcpGameServer(minaServerConfig_game);
		
		//连接Cluster集群客户端
		MinaClientConfig minaClientConfig_cluster=FileUtil.getConfigXML(StartGate.getConfigPath(),
				"minaClientConfig_cluster.xml", MinaClientConfig.class);
		if(minaClientConfig_cluster==null) {
			logger.error("mina 连接集群配置不存在");
			System.exit(1);
		}
		clusterClient=new Gate2ClusterClient(minaClientConfig_cluster);
		Config.SERVER_ID=minaServerConfig_user.getId();
		

		MinaServerConfig minaServerConfig_udp_user=FileUtil.getConfigXML(StartGate.getConfigPath(), 
				"minaServerConfig_udp_user.xml",MinaServerConfig.class);
		MinaServerConfig minaServerConfig_websocket_user=FileUtil.getConfigXML(StartGate.getConfigPath(), 
				"minaServerConfig_websocket_user.xml", MinaServerConfig.class);
		MinaServerConfig minaServerConfig_http=FileUtil.getConfigXML(StartGate.getConfigPath(), 
				"minaServerConfig_http.xml", MinaServerConfig.class);
		//开启udp服务
		if(minaServerConfig_udp_user!=null) {
			gateUdpUserServer=new GateUdpUserServer(minaServerConfig_udp_user);
		}
		//开启websocket
		if(minaServerConfig_websocket_user!=null) {
			gateWebSocketUserServer=new GateWebSocketUserServer(minaServerConfig_websocket_user);
		}
		//http服务
		gateHttpServer=new GateHttpServer(minaServerConfig_http);
		
		//服务器启动时间 测试redis
		String startTimeKey=GateKey.GM_Gate_StartTime.getKey(minaServerConfig_game.getId());
//		if(JedisManager.getJedisCluster().exists(startTimeKey)) {
//			JedisManager.getJedisCluster().set(startTimeKey,""+System.currentTimeMillis());
//		}
	}
	
	public static GateServer getInstance() {
		return StartGate.getGateServer();
	}
	
	@Override
	public void run() {
		new Thread(gateTcpUserServer).start();
		new Thread(clusterClient).start();
		new Thread(gateTcpGameServer).start();
		new Thread(gateHttpServer).start();
		if(gateUdpUserServer!=null) {
			new Thread(gateUdpUserServer).start();
		}
		if(gateWebSocketUserServer!=null) {
			new Thread(gateWebSocketUserServer).start();
		}
	}
	
	/**
	 * 构建服务器更新注册消息
	 * @param minaServerConfig
	 */
	public ServerRegisterRequest buildServerRegisterRequest(MinaServerConfig minaServerConfig) {
		ServerRegisterRequest.Builder builder=ServerRegisterRequest.newBuilder();
		com.jbm.game.message.ServerMessage.ServerInfo.Builder info=com.jbm.game.message.ServerMessage.ServerInfo.newBuilder();
		info.setId(minaServerConfig.getId());
		info.setIp(minaServerConfig.getIp());
		info.setMaxUserCount(1000);
		info.setOnline(UserSessionManager.getInstance().getOnlineCount());
		info.setName(minaServerConfig.getName());
		info.setState(ServerState.NORMAL.getState());
		info.setType(minaServerConfig.getType().getType());
		info.setWwwip("");
		info.setPort(minaServerConfig.getPort());
		info.setHttpport(minaServerConfig.getHttpPort());
		info.setFreeMemory(SysUtil.freeMemory());
		info.setVersion(minaServerConfig.getVersion());
		info.setTotalMemory(SysUtil.totalMemory());
		builder.setServerInfo(info);
		return builder.build();
	}

	public GateTcpUserServer getGateTcpUserServer() {
		return gateTcpUserServer;
	}

	public Gate2ClusterClient getClusterClient() {
		return clusterClient;
	}
	
	
}
