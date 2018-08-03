package com.jbm.game.bydr.server;

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
 * http 服务器
 * @author JiangBangMing
 *
 * 2018年8月2日 下午7:48:30
 */
public class BydrHttpServer extends GameHttpSevice{

	public BydrHttpServer(MinaServerConfig minaServerConfig) {
		super(minaServerConfig);
	}

	@Override
	protected void running() {
		super.running();
		// 添加关服、脚本加载 等公用 handler
		ScriptManager.getInstance().addIHandler(ReloadScriptHandler.class);
		ScriptManager.getInstance().addIHandler(ExitServerHandler.class);
		ScriptManager.getInstance().addIHandler(ReloadConfigHandler.class);
		ScriptManager.getInstance().addIHandler(JvmInfoHandler.class);
		ScriptManager.getInstance().addIHandler(GetFaviconHandler.class);
		ScriptManager.getInstance().addIHandler(ThreadInfoHandler.class);
	}
}
