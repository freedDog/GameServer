package com.jbm.game.hall.tcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.thread.ThreadType;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.ServerMessage.ServerRegisterResponse;

/**
 * 服务器注册消息返回
 * @author JiangBangMing
 *
 * 2018年7月25日 下午8:31:20
 */
@HandlerEntity(mid=MID.ServerRegisterRes_VALUE,msg=ServerRegisterResponse.class,thread=ThreadType.SYNC)
public class ServerRegisterHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(ServerRegisterHandler.class);
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
