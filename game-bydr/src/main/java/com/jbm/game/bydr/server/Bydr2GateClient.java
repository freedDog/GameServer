package com.jbm.game.bydr.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.thread.RoomExecutor;
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
import com.jbm.game.engine.util.SysUtil;
import com.jbm.game.message.ServerMessage;
import com.jbm.game.message.ServerMessage.ServerRegisterRequest;
import com.jbm.game.model.script.IGameServerCheckScript;

/**
 * 连接大厅  tcp 客户端
 * @author JiangBangMing
 *
 * 2018年8月2日 下午4:47:37
 */
public class Bydr2GateClient extends MutilMinaTcpClientService{
	
	private static final Logger logger=LoggerFactory.getLogger(Bydr2ClusterClient.class);
	
	public Bydr2GateClient(ThreadPoolExecutorConfig threadPoolExecutorConfig, MinaClientConfig minaClientConfig) {
		super(threadPoolExecutorConfig, minaClientConfig);
	}

	@Override
	protected void running() {
		//全局同步线程
		ServerThread syncThread=getExecutor(ThreadType.SYNC);
		syncThread.addTimerEvent(new ServerHeartTimer());
		
		//添加房间线程池
		RoomExecutor roomExecutor=new RoomExecutor();
		getServerThreads().put(ThreadType.ROOM, roomExecutor);
	}
	
	/**
	 * 消息处理器
	 * @author JiangBangMing
	 *
	 * 2018年8月2日 下午6:15:58
	 */
	public class MutilConHallHandler extends MutilTcpProtocolHandler{
		
		public MutilConHallHandler(ServerInfo serverInfo, MinaClientService service) {
			super(serverInfo, service);
		}
		
		@Override
		public void sessionOpened(IoSession session) {
			super.sessionOpened(session);
			//向网关服注册session
			ServerRegisterRequest.Builder builder=ServerRegisterRequest.newBuilder();
			ServerMessage.ServerInfo.Builder info=ServerMessage.ServerInfo.newBuilder();
			info.setId(getMinaClientConfig().getId());
			info.setIp("");
			info.setMaxUserCount(1000);
			info.setOnline(1);
			info.setName(getMinaClientConfig().getName());
			info.setState(ServerState.NORMAL.getState());
			info.setType(getMinaClientConfig().getType().getType());
			info.setWwwip("");
			info.setTotalMemory(SysUtil.totalMemory());
			info.setFreeMemory(SysUtil.freeMemory());
			ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IGameServerCheckScript.class, 
					script -> script.buildServerInfo(info));
			builder.setServerInfo(info);
			session.write(new IDMessage(session, builder.build(), 0));
		}
	}
}

