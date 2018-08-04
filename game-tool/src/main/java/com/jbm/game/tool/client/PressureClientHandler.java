package com.jbm.game.tool.client;

import org.apache.mina.core.session.IoSession;

import com.jbm.game.engine.mina.handler.ClientProtocolHandler;
import com.jbm.game.engine.mina.service.SingleMinaTcpClientService;
import com.jbm.game.engine.server.BaseServerConfig;
import com.jbm.game.engine.server.Service;

/**
 * 压测消息处理
 * @author JiangBangMing
 *
 * 2018年8月4日 下午2:51:24
 */
public class PressureClientHandler extends ClientProtocolHandler{
	
	private SingleMinaTcpClientService service;
	public PressureClientHandler() {
		super(8);
	}

	@Override
	public void sessionOpened(IoSession session) {
		super.sessionOpened(session);
		service.onIoSessionConnect(session);
	}

	public void setService(SingleMinaTcpClientService service) {
		this.service = service;
	}

	@Override
	public Service<? extends BaseServerConfig> getService() {
		return this.service;
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
//		IoFilter filter = session.getFilterChain().get(SslFilter.class);
//		if(filter!=null){
//			SslFilter sslFilter=(SslFilter)filter;
//			sslFilter.setUseClientMode(true);
//			sslFilter.startSsl(session);
//			sslFilter.initiateHandshake(session);
//		}
	}

	@Override
	public void messageReceived(IoSession session, Object obj) throws Exception {
		super.messageReceived(session, obj);
	}

}
