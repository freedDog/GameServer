package com.jbm.game.gate.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.firewall.BlacklistFilter;

import com.jbm.game.engine.mina.TcpServer;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.mina.websocket.WebSocketCodecFactory;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.server.Service;
import com.jbm.game.gate.script.IGateServerScript;
import com.jbm.game.gate.server.handler.GateWebSocketUserServerHandler;

/**
 * 网关 用户webSocket 服务器(网页连接)
 * @author JiangBangMing
 *
 * 2018年7月22日 下午2:43:49
 */
public class GateWebSocketUserServer extends Service<MinaServerConfig>{

	private final TcpServer tcpServer;
	private final MinaServerConfig minaServerConfig;
	Map<String, IoFilter> filters=new HashMap<>();
	private BlacklistFilter blacklistFilter;//IP 黑名单过滤器
	private GateWebSocketUserServerHandler gateWebSocketUserServerHandler;
	
	public GateWebSocketUserServer(MinaServerConfig minaServerConfig) {
		super(null);
		this.minaServerConfig=minaServerConfig;
		blacklistFilter=new BlacklistFilter();
		filters.put("Blacklist", blacklistFilter);
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IGateServerScript.class,
				script -> script.setIpBlackList(blacklistFilter));
		gateWebSocketUserServerHandler=new GateWebSocketUserServerHandler();
		tcpServer=new TcpServer(minaServerConfig, gateWebSocketUserServerHandler,new WebSocketCodecFactory(),filters);
	}
	
	@Override
	protected void running() {
		gateWebSocketUserServerHandler.setService(this);
		tcpServer.run();
	}
	
	@Override
	protected void onShutdown() {
		super.onShutdown();
		tcpServer.stop();
	}

	public MinaServerConfig getMinaServerConfig() {
		return minaServerConfig;
	}
	
	
	
}
