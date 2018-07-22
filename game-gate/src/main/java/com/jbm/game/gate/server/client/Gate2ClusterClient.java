package com.jbm.game.gate.server.client;

import com.jbm.game.engine.mina.config.MinaClientConfig;
import com.jbm.game.engine.mina.service.SingleMinaTcpClientService;

/**
 * 连接到集群管理
 * @author JiangBangMing
 *
 * 2018年7月21日 下午1:06:39
 */
public class Gate2ClusterClient extends SingleMinaTcpClientService{

	public Gate2ClusterClient(MinaClientConfig minaClientConfig) {
		super(minaClientConfig);
	}
}
