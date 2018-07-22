package com.jbm.game.gate.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.firewall.BlacklistFilter;

import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.mina.service.ClientServerService;
import com.jbm.game.engine.thread.ThreadPoolExecutorConfig;
import com.jbm.game.gate.manager.UserSessionManager;
import com.jbm.game.gate.server.handler.GateTcpUserServerHandler;

/**
 * 网关用户TCP服务器
 * @author JiangBangMing
 *
 * 2018年7月22日 下午2:26:34
 */
public class GateTcpUserServer extends ClientServerService{

	private static Map<String, IoFilter> filters=new HashMap<>();
	private static BlacklistFilter blacklistFilter=new BlacklistFilter();//IP黑名单过滤器
	
	static {
		filters.put("Blacklist", blacklistFilter);
	}
	
	public GateTcpUserServer(ThreadPoolExecutorConfig threadPoolExecutorConfig,MinaServerConfig minaServerConfig) {
		super(threadPoolExecutorConfig,minaServerConfig,new GateTcpUserServerHandler(),filters);
	}

	public static BlacklistFilter getBlacklistFilter() {
		return blacklistFilter;
	}
	
	@Override
	protected void onShutdown() {
		UserSessionManager.getInstance().onShutdown();
		super.onShutdown();
	}
	
	
}
