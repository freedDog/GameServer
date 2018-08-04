package com.jbm.game.tool.client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JTextArea;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.engine.math.MathUtil;
import com.jbm.game.engine.mina.MinaUdpClient;
import com.jbm.game.engine.mina.code.ClientProtocolCodecFactory;
import com.jbm.game.engine.mina.config.MinaClientConfig;
import com.jbm.game.engine.mina.config.MinaClientConfig.MinaClienConnToConfig;
import com.jbm.game.engine.mina.service.SingleMinaTcpClientService;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.util.HttpUtil;
import com.jbm.game.tool.client.ssl.ClientSslContextFactory;

/**
 * 压力测试客户端
 * @author JiangBangMing
 *
 * 2018年8月4日 下午2:37:19
 */
public class PressureClientTool {

	private static final Logger logger=LoggerFactory.getLogger(PressureClientTool.class);
	
	public static Map<String, Player> players=new ConcurrentHashMap<>();
	public static String configPath;
	
	//压力测试客户端个数
	private int clientNum=1;
	
	//用户名前缀
	private String userNamePrefix="jbm";
	//用户名编号
	private static AtomicInteger userNameNo=new AtomicInteger(1);
	//集群Ip
	private String clusterIp="192.168.14.94";
	
	public PressureClientTool(int clientNum,String userNamePrefix,String password,String clusterIp,JTextArea logTextArea) {
		this.clientNum=clientNum;
		this.clusterIp=clusterIp;
		this.userNamePrefix=userNamePrefix;
		initConfigPath();
		ScriptManager.getInstance().init(null);
		
		//循环初始化客户端
		try {
			for(int i=0;i<clientNum;i++) {
				PressureClientHandler pressureClientHandler=new PressureClientHandler();
				MinaClientConfig minaClientConfig=getMinaClientConfig();
				String userName=userNamePrefix+userNameNo.incrementAndGet();
				//Tcp 添加SSL
				Map<String, IoFilter> filters=new HashMap<>();
//				SslFilter sslFilter=new SslFilter(ClientSslContextFactory.getInstance(false));
				SingleMinaTcpClientService service=new SingleMinaTcpClientService(minaClientConfig,
						new ClientProtocolCodecFactory(),pressureClientHandler,filters);
				pressureClientHandler.setService(service);
				new Thread(service).start();
				logTextArea.append(String.format("用户%s开始连接服务器...\n",userName));
				//UDP
//				MinaClientConfig minaClientConfig2=new MinaClientConfig();
//				MinaClienConnToConfig connTo=new MinaClienConnToConfig();
//				connTo.setHost(minaClientConfig.getConnTo().getHost());
//				connTo.setPort(minaClientConfig.getConnTo().getPort());
//				minaClientConfig2.setConnTo(connTo);
//				MinaUdpClient udpClient=new MinaUdpClient(minaClientConfig2, pressureClientHandler, 
//						new ClientProtocolCodecFactory());
//				new Thread(udpClient).start();
				
//				while(udpClient.getSession()==null) {
//					Thread.sleep(MathUtil.random(500,3000));
//				}
				while(service.getMostIdleIoSession()==null) {
					Thread.sleep(MathUtil.random(500,3000));
				}
                Player player = new Player();
                player.setUserName(userName);
                player.setPassword(password);
//                player.setUdpSession(udpClient.getSession());
                player.setTcpSession(service.getMostIdleIoSession());
                player.setLogTextArea(logTextArea);
                if(player.getTcpSession()==null) {//||player.getUdpSession()==null){
                    logger.warn("用户{}连接服务器失败",userName);
                    logTextArea.append(String.format("用户%s连接服务器失败\n",userName));
                    continue;
                }
                player.loginInit();
                players.put(userName, player);
                logTextArea.append(String.format("用户%s连接服务器成功\n",userName));
                new PressureServiceThread(player,logTextArea).start();
                
			}
		}catch (Exception e) {
			logTextArea.append(e.getMessage());
			logger.error("",e);
		}
	}
	
	
	public MinaClientConfig getMinaClientConfig() {
		MinaClientConfig minaClientConfig=new MinaClientConfig();
		try {
			minaClientConfig.setOrderedThreadPoolExecutorSize(1);
			MinaClienConnToConfig connTo=new MinaClienConnToConfig();
//			String url="http://"+this.clusterIp+":8001/server/gate/ip";
//			String get=HttpUtil.URLGet(url);
//			logger.info("hall host:{}",get);
//			if(get.contains(":")) {
//				String[] split=get.split(":");
//				connTo.setHost(split[0]);
//				connTo.setPort(Integer.parseInt(split[1]));
//			}else {
//				logger.warn("大厅IP获取失败");
//			}
			connTo.setHost("127.0.0.1");
			connTo.setPort(9002);
			minaClientConfig.setConnTo(connTo);
		}catch (Exception e) {
			logger.error("获取大厅端口",e);
		}
		return minaClientConfig;
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
