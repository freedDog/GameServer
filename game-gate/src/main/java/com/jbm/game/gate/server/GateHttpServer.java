package com.jbm.game.gate.server;

import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.mina.service.GameHttpSevice;

/**
 * http 服务器
 * @author JiangBangMing
 *
 * 2018年7月22日 下午2:34:41
 */
public class GateHttpServer extends GameHttpSevice{

	public GateHttpServer(MinaServerConfig minaServerConfig) {
		super(minaServerConfig);
	}
	
	@Override
	protected void running() {
		super.running();
		// 添加关服、脚本加载 等公用 handler
	}
}
