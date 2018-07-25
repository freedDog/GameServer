package com.jbm.game.gate.tcp.server;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.thread.ThreadType;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.ServerMessage.ServerRegisterResponse;

/**
 * 注册集群服返回
 * @author JiangBangMing
 *
 * 2018年7月25日 下午12:37:48
 */
@HandlerEntity(mid=MID.ServerRegisterRes_VALUE,msg=ServerRegisterResponse.class,thread=ThreadType.SYNC)
public class ServerRegisterResHandler extends TcpHandler{

	@Override
	public void run() {
		
	}
}
