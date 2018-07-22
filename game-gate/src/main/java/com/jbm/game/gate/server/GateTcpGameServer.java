package com.jbm.game.gate.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.mina.TcpServer;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.server.Service;
import com.jbm.game.gate.server.handler.GateTcpGameServerHandler;

/**
 * 子游戏连接服务
 *  <p>游戏服、大厅服等内部共用的服务器</p>
 * @author JiangBangMing
 *
 * 2018年7月21日 下午2:11:04
 */
public class GateTcpGameServer extends Service<MinaServerConfig>{

	private static final Logger logger=LoggerFactory.getLogger(GateTcpGameServer.class);
	private TcpServer tcpServer;
	private MinaServerConfig minaServerConfig;
	
	public GateTcpGameServer(MinaServerConfig minaServerConfig) {
		super(null);
		this.minaServerConfig=minaServerConfig;
		tcpServer=new TcpServer(minaServerConfig, new GateTcpGameServerHandler(this));
	}
	
	@Override
	protected void running() {
		this.tcpServer.run();
	}
	
	@Override
	protected void onShutdown() {
		super.onShutdown();
		this.tcpServer.stop();
	}
	
	public MinaServerConfig getMinaServerConfig() {
		return this.minaServerConfig;
	}
}
