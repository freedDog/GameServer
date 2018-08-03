package com.jbm.game.bydr.server;


import com.jbm.game.bydr.thread.RoomExecutor;
import com.jbm.game.engine.netty.config.NettyClientConfig;
import com.jbm.game.engine.netty.service.MutilNettyTcpClientService;
import com.jbm.game.engine.thread.ServerThread;
import com.jbm.game.engine.thread.ThreadPoolExecutorConfig;
import com.jbm.game.engine.thread.ThreadType;
import com.jbm.game.engine.thread.timer.event.ServerHeartTimer;

/**
 * netty 连接到网关服
 * @author JiangBangMing
 *
 * 2018年8月2日 下午7:44:42
 */
public class Bydr2GateClientNetty extends MutilNettyTcpClientService{

	public Bydr2GateClientNetty(NettyClientConfig nettyClientConfig) {
		super(nettyClientConfig);
	}
	
	public Bydr2GateClientNetty(ThreadPoolExecutorConfig threadPoolExecutorConfig,NettyClientConfig clientConfig) {
		super(threadPoolExecutorConfig, clientConfig);
	}
	
	@Override
	protected void initThread() {
		super.initThread();
		//全局同步线程
		ServerThread syncThread=getExecutor(ThreadType.SYNC);
		syncThread.addTimerEvent(new ServerHeartTimer());
		
		//添加房间线程池
		RoomExecutor roomExecutor=new RoomExecutor();
		getServerThreads().put(ThreadType.ROOM, roomExecutor);	
	}

}
