package com.jbm.game.hall.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.mina.config.MinaClientConfig;
import com.jbm.game.engine.mina.message.IDMessage;
import com.jbm.game.engine.mina.service.MinaClientService;
import com.jbm.game.engine.mina.service.MutilMinaTcpClientService;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.engine.server.ServerState;
import com.jbm.game.engine.thread.ServerThread;
import com.jbm.game.engine.thread.ThreadPoolExecutorConfig;
import com.jbm.game.engine.thread.ThreadType;
import com.jbm.game.engine.thread.timer.event.ServerHeartTimer;
import com.jbm.game.message.ServerMessage;
import com.jbm.game.message.ServerMessage.ServerRegisterRequest;
import com.jbm.game.model.constant.NetPort;
import com.jbm.game.model.script.IGameServerCheckScript;

/**
 * 捕鱼达人连接大厅 Tcp 客户端
 * @author JiangBangMing
 *
 * 2018年7月24日 下午1:38:59
 */
public class Hall2GateClient extends MutilMinaTcpClientService{
	
	private static final Logger logger=LoggerFactory.getLogger(Hall2GateClient.class);
	
	public Hall2GateClient(ThreadPoolExecutorConfig threadPoolExecutorConfig,MinaClientConfig minaClientConfig) {
		super(threadPoolExecutorConfig,minaClientConfig);
	}
	
	@Override
	protected void running() {
		ServerThread syncThread=getExecutor(ThreadType.SYNC);
		syncThread.addTimerEvent(new ServerHeartTimer());
	}
	
	/**
	 * 更新大厅服务器
	 * @param info
	 */
	public void updateHallServerInfo(ServerMessage.ServerInfo info) {
		ServerInfo serverInfo=serverMap.get(info.getId());
		if(serverInfo==null) {
			serverInfo=getServerInfo(info);
			addTcpClient(serverInfo, NetPort.GATE_GAME_PORT,new MutilTcpProtocolHandler(serverInfo, this));
		}else {
			serverInfo.setIp(info.getIp());
			serverInfo.setId(info.getId());
			serverInfo.setPort(info.getPort());
			serverInfo.setState(info.getState());
			serverInfo.setOnline(info.getOnline());
			serverInfo.setMaxUserCount(info.getMaxUserCount());
			serverInfo.setName(info.getName());
			serverInfo.setHttpPort(info.getHttpport());
			serverInfo.setWwwip(info.getWwwip());
		}
		serverMap.put(serverInfo.getId(), serverInfo);
	}
	
	private ServerInfo getServerInfo(ServerMessage.ServerInfo info) {
		ServerInfo serverInfo=new ServerInfo();
		serverInfo.setIp(info.getIp());
		serverInfo.setId(info.getId());
		serverInfo.setPort(info.getPort());
		serverInfo.setState(info.getState());
		serverInfo.setOnline(info.getOnline());
		serverInfo.setMaxUserCount(info.getMaxUserCount());
		serverInfo.setName(info.getName());
		serverInfo.setHttpPort(info.getHttpport());
		serverInfo.setWwwip(info.getWwwip());
		return serverInfo;
	}

	/**
	 * 消息处理器
	 * @author JiangBangMing
	 *
	 * 2018年7月24日 下午3:27:59
	 */
	public class MutilConHallHandler extends MutilTcpProtocolHandler{
		
		public MutilConHallHandler(ServerInfo serverInfo,MinaClientService service) {
			super(serverInfo, service);
		}
		
		@Override
		public void sessionOpened(IoSession session) {
			super.sessionOpened(session);
			//向大厅服注册session TODO
			ServerRegisterRequest.Builder builder=ServerRegisterRequest.newBuilder();
			ServerMessage.ServerInfo.Builder info=ServerMessage.ServerInfo.newBuilder();
			info.setId(getMinaClientConfig().getId());
			info.setIp("");
			info.setMaxUserCount(1000);
			info.setOnline(1);
			info.setName(getMinaClientConfig().getName());
			info.setState(ServerState.NORMAL.getState());
			info.setType(getMinaClientConfig().getType().getType());
			ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IGameServerCheckScript.class, 
					script -> script.buildServerInfo(info));
			
			builder.setServerInfo(info);
			session.write(new IDMessage(session, builder.build(), 0));
			
		}
	}
	
}
