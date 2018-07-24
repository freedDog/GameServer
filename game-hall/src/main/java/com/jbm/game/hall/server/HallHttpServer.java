package com.jbm.game.hall.server;

import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.mina.service.GameHttpSevice;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.model.handler.http.favicon.GetFaviconHandler;
import com.jbm.game.model.handler.http.server.ExitServerHandler;
import com.jbm.game.model.handler.http.server.JvmInfoHandler;
import com.jbm.game.model.handler.http.server.ReloadConfigHandler;
import com.jbm.game.model.handler.http.server.ReloadScriptHandler;
import com.jbm.game.model.handler.http.server.ThreadInfoHandler;

/**
 * 大厅http 服务
 * @author JiangBangMing
 *
 * 2018年7月24日 下午1:35:56
 */
public class HallHttpServer extends GameHttpSevice{

	public HallHttpServer(MinaServerConfig minaServerConfig) {
		super(minaServerConfig);
	}
	
	@Override
	protected void running() {
		super.running();
		ScriptManager.getInstance().addIHandler(ReloadScriptHandler.class);
		ScriptManager.getInstance().addIHandler(ExitServerHandler.class);
		ScriptManager.getInstance().addIHandler(ReloadConfigHandler.class);
		ScriptManager.getInstance().addIHandler(GetFaviconHandler.class);
		ScriptManager.getInstance().addIHandler(ThreadInfoHandler.class);
		ScriptManager.getInstance().addIHandler(JvmInfoHandler.class);
	}
}
