package com.jbm.game.gate.tcp.role;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.mina.message.IDMessage;
import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.engine.util.MsgUtil;
import com.jbm.game.gate.manager.ServerManager;
import com.jbm.game.gate.manager.UserSessionManager;
import com.jbm.game.gate.server.GateServer;
import com.jbm.game.gate.struct.UserSession;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.hall.HallLoginMessage.LoginRequest;
import com.jbm.game.message.system.SystemMessage.SystemErrorCode;
import com.jbm.game.message.system.SystemMessage.SystemErrorResponse;

/**
 * 登录请求
 * <p>
 * 保存用户session 设置UserSession 大厅ID，大厅session<br>
 * TODO 重连处理？？？？
 * @author JiangBangMing
 *
 * 2018年7月24日 下午7:31:22
 */

@HandlerEntity(mid=MID.LoginReq_VALUE,desc="登陆",msg=LoginRequest.class)
public class LoginReqHandler extends TcpHandler{
	private static final Logger logger=LoggerFactory.getLogger(LoginReqHandler.class);

	@Override
	public void run() {
		LoginRequest request=getMsg();
		UserSession userSession=UserSessionManager.getInstance().getUserSessionBySessionId(session.getId());
		if(userSession==null) {
			session.write(ServerManager.getInstance().buildSystemErrorResponse(SystemErrorCode.ConectReset,"连接会话已失效，请重连"));
			logger.warn("连接会话已失效，请重连");
			return;
		}
		ServerInfo serverInfo=ServerManager.getInstance().getIdleGameServer(ServerType.HALL, userSession);
		if(serverInfo==null) {
			SystemErrorResponse.Builder sysBuilder=SystemErrorResponse.newBuilder();
			sysBuilder.setErrorCode(SystemErrorCode.HallNotFind);
			sysBuilder.setMsg("未开启大厅服");
			getSession().write(sysBuilder.build());
			logger.warn("大厅服不可用");
			return;
		}
		IoSession hallSession=serverInfo.getMostIdleIoSession();
		LoginRequest.Builder builder=request.toBuilder();
		builder.setSessionId(session.getId());
		builder.setIp(MsgUtil.getIp(session));
		builder.setGateId(GateServer.getInstance().getGateTcpUserServer().getMinaServerConfig().getId());
		if(serverInfo==null||hallSession==null) {
			logger.warn("大厅服务器未准备就绪");
			session.write(ServerManager.getInstance().buildSystemErrorResponse(SystemErrorCode.HallNotFind, "没有可用大厅服"));
			return;
		}
		
		userSession.setHallServerId(serverInfo.getId());
		userSession.setHallSession(hallSession);
		userSession.setVersion(request.getVersion());
		IDMessage idMessage=new IDMessage(hallSession, builder.build(), 0);
		idMessage.run();
	}
	
}
