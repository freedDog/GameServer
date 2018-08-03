package com.jbm.game.bydr.server;

import com.jbm.game.engine.mina.config.MinaClientConfig;
import com.jbm.game.engine.mina.service.SingleMinaTcpClientService;

/**
 * 连接集群tcp 客户端
 * @author JiangBangMing
 *
 * 2018年8月2日 下午4:45:27
 */
public class Bydr2ClusterClient extends SingleMinaTcpClientService{

	public Bydr2ClusterClient(MinaClientConfig minaClientConfig) {
		super(minaClientConfig);
	}
	
	@Override
	protected void running() {
		// TODO Auto-generated method stub
		super.running();
	}
}
