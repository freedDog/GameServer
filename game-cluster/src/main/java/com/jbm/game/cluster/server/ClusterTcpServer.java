package com.jbm.game.cluster.server;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.cluster.manager.ServerManager;
import com.jbm.game.engine.mina.TcpServer;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.mina.handler.DefaultProtocolHandler;
import com.jbm.game.engine.server.BaseServerConfig;
import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.engine.server.Service;
import com.jbm.game.engine.thread.ServerThread;
import com.jbm.game.engine.thread.ThreadPoolExecutorConfig;
import com.jbm.game.engine.thread.ThreadType;
import com.jbm.game.engine.thread.timer.event.ServerHeartTimer;
import com.jbm.game.engine.util.IntUtil;
import com.jbm.game.engine.util.MsgUtil;

/**
 * TCP 连接
 * @author JiangBangMing
 *
 * 2018年7月24日 上午10:41:57
 */
public class ClusterTcpServer extends Service<MinaServerConfig>{

	private static final Logger logger=LoggerFactory.getLogger(ClusterTcpServer.class);
	private final TcpServer minaServer;
	private final MinaServerConfig minaServerConfig;
	public static final String SERVER_INFO="serverInfo";//服务器信息
	
	public ClusterTcpServer(ThreadPoolExecutorConfig threadPoolExecutorConfig,MinaServerConfig minaServerConfig) {
		super(threadPoolExecutorConfig);
		this.minaServerConfig=minaServerConfig;
		minaServer=new TcpServer(minaServerConfig, new ClusterTcpServerHanhler(this));
	}
	
	@Override
	protected void running() {
		logger.info("rung... ");
		minaServer.run();
		ServerThread syncThread=getExecutor(ThreadType.SYNC);
		syncThread.addTimerEvent(new ServerHeartTimer());
	}

	@Override
	protected void onShutdown() {
		super.onShutdown();
		logger.info(" stop ...");
		minaServer.stop();
	}
	@Override
	public String toString() {
		return minaServerConfig.getName();
	}
	public int getId() {
		return minaServerConfig.getId();
	}
	
	public String getName() {
		return minaServerConfig.getName();
	}
	
	public String getWeb() {
		return minaServerConfig.getChannel();
	}
	
	/**
	 * 消息处理
	 * @author JiangBangMing
	 *
	 * 2018年7月24日 下午12:16:22
	 */
	public class ClusterTcpServerHanhler extends DefaultProtocolHandler{
		private final Service<MinaServerConfig> service;
		
		public ClusterTcpServerHanhler(Service<MinaServerConfig> service) {
			super(4);//消息ID+消息内容
			this.service=service;
		}
		
		@Override
		public void sessionIdle(IoSession session, IdleStatus idleStatus) throws Exception {
			// 客户端长时间不发送请求，将断开链接LoginTcpServer->minaServerConfig->readerIdleTime
			// 60  1分钟
			MsgUtil.close(session, "连接空闲:"+session.toString()+" "+idleStatus.toString());
		}
		
		@Override
		public void sessionClosed(IoSession session) throws Exception {
			super.sessionClosed(session);
			ServerInfo serverInfo=(ServerInfo)session.getAttribute(SERVER_INFO);
			if(serverInfo!=null) {
				logger.info("服务器:"+serverInfo.getName()+"断开连接");
				ServerManager.getInstance().removeServer(serverInfo);
			}else {
				logger.error("未知游戏服务器断开连接");
			}
		}
		
		@Override
		protected void forward(IoSession session, int msgID, byte[] bytes) {
			logger.warn("无法找到消息处理器:msgID{},bytes{}",msgID,IntUtil.BytesToStr(bytes));
		}
		
		@Override
		public Service<? extends BaseServerConfig> getService() {
			return this.service;
		}
	}
}
