package com.jbm.game.hall.server;

import com.jbm.game.engine.mina.config.MinaClientConfig;
import com.jbm.game.engine.mina.service.SingleMinaTcpClientService;

/**
 * 连接集群tcp 客户端
 * @author JiangBangMing
 *
 * 2018年7月24日 下午3:30:57
 */
public class Hall2ClusterClient extends SingleMinaTcpClientService{

	public Hall2ClusterClient(MinaClientConfig minaClientConfig) {
		super(minaClientConfig);
	}
	
	@Override
	protected void running() {
		super.running();
	}

}
