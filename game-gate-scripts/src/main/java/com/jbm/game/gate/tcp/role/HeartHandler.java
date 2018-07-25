package com.jbm.game.gate.tcp.role;


import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.util.TimeUtil;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.system.SystemMessage.HeartRequest;
import com.jbm.game.message.system.SystemMessage.HeartResponse;

/**
 * 心跳
 * @author JiangBangMing
 *
 * 2018年7月25日 上午10:30:10
 */
@HandlerEntity(mid=MID.HeartReq_VALUE,msg=HeartRequest.class)
public class HeartHandler extends TcpHandler{

	@Override
	public void run() {
		HeartResponse.Builder builder=HeartResponse.newBuilder();
		builder.setServerTime(TimeUtil.currentTimeMillis());
		session.write(builder.build());
	}
	
}
