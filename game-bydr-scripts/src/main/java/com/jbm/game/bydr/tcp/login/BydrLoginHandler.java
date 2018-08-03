package com.jbm.game.bydr.tcp.login;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.manager.RoleManager;
import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.hall.HallLoginMessage.LoginSubGameRequest;
import com.jbm.game.message.hall.HallLoginMessage.LoginSubGameResponse;
import com.jbm.game.model.constant.Reason;

/**
 * 登录
 * TODO 此次全用的是session写逻辑，用netty需要使用channel
 * @author JiangBangMing
 *
 * 2018年8月3日 下午4:30:49
 */
@HandlerEntity(mid=MID.LoginSubGameReq_VALUE,msg=LoginSubGameRequest.class)
public class BydrLoginHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(BydrLoginHandler.class);
	
	@Override
	public void run() {
		LoginSubGameRequest req=getMsg();
		
		switch (req.getType()) {
		case 0://登录
			RoleManager.getInstance().login(req.getRid(), Reason.UserLogin, role ->{
				role.setIoSession(getSession());
				role.setChannel(getChannel());
			});
			break;
		case 1://重连
			RoleManager.getInstance().login(req.getRid(), Reason.UserReconnect, role ->{
				role.setIoSession(getSession());
				role.setChannel(getChannel());
			});
			break;
		case 2://跨服登录
			RoleManager.getInstance().login(req.getRid(), Reason.CrossServer, role ->{
				role.setIoSession(getSession());
				role.setChannel(getChannel());
			});
			break;
		default:
			break;
		}
		if(req.getType()==2) {//跨服不返回消息
			logger.info("角色[{}] 跨服登录",req.getRid());
			return;
		}
		LoginSubGameResponse.Builder builder=LoginSubGameResponse.newBuilder();
		builder.setResult(1);
		sendIdMsg(builder.build());
	}
}
