package com.jbm.game.cluster.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.mina.HttpServer;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.mina.handler.HttpServerIoHandler;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.server.Service;
import com.jbm.game.engine.thread.ThreadPoolExecutorConfig;
import com.jbm.game.model.handler.http.favicon.GetFaviconHandler;
import com.jbm.game.model.handler.http.server.ExitServerHandler;
import com.jbm.game.model.handler.http.server.JvmInfoHandler;
import com.jbm.game.model.handler.http.server.ReloadConfigHandler;
import com.jbm.game.model.handler.http.server.ReloadScriptHandler;
import com.jbm.game.model.handler.http.server.ThreadInfoHandler;

/**
 * http 连接
 * @author JiangBangMing
 *
 * 2018年7月24日 下午12:19:55
 */
public class ClusterHttpServer extends Service<MinaServerConfig>{

	private static final Logger logger=LoggerFactory.getLogger(ClusterHttpServer.class);
	
	private final HttpServer httpServer;
	private final MinaServerConfig minaServerConfig;
	
	public ClusterHttpServer(ThreadPoolExecutorConfig threadPoolExecutorConfig,MinaServerConfig minaServerConfig) {
		super(threadPoolExecutorConfig);
		this.minaServerConfig=minaServerConfig;
		this.httpServer=new HttpServer(minaServerConfig, new ClusterHttpServerHandler(this));
	}
	
	@Override
	protected void running() {
		logger.info(" run ...");
		httpServer.run();
		ScriptManager.getInstance().addIHandler(ReloadScriptHandler.class);
		ScriptManager.getInstance().addIHandler(ExitServerHandler.class);
		ScriptManager.getInstance().addIHandler(ReloadConfigHandler.class);
		ScriptManager.getInstance().addIHandler(GetFaviconHandler.class);
		ScriptManager.getInstance().addIHandler(ThreadInfoHandler.class);
		ScriptManager.getInstance().addIHandler(JvmInfoHandler.class);
	}
	
	@Override
	protected void onShutdown() {
		super.onShutdown();
		logger.info(" stop ...");
		httpServer.stop();
	}
	
	@Override
	public String toString() {
		return minaServerConfig.getName();
	}
	public class ClusterHttpServerHandler extends HttpServerIoHandler{
		private Service<MinaServerConfig> service;
		
		public ClusterHttpServerHandler(Service<MinaServerConfig> service) {
			this.service=service;
		}
		
		@Override
		protected Service<MinaServerConfig> getService() {
			return this.service;
		}
	}
}
