package com.jbm.game.bydr.script.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.server.BydrServer;
import com.jbm.game.engine.mina.message.IDMessage;
import com.jbm.game.engine.netty.config.NettyClientConfig;
import com.jbm.game.engine.netty.handler.DefaultClientInBoundHandler;
import com.jbm.game.engine.netty.handler.IChannelHandlerScript;
import com.jbm.game.engine.netty.service.MutilNettyTcpClientService;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.server.BaseServerConfig;
import com.jbm.game.engine.server.IMutilTcpClientService;
import com.jbm.game.engine.server.ServerState;
import com.jbm.game.engine.server.Service;
import com.jbm.game.message.ServerMessage;
import com.jbm.game.message.ServerMessage.ServerRegisterRequest;
import com.jbm.game.model.script.IGameServerCheckScript;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * netty连接网关客户端消息处理脚本
 * @author JiangBangMing
 *
 * 2018年8月3日 下午3:00:27
 */
public class GateClientInBoundHandlerScript implements IChannelHandlerScript{

	private static final Logger logger=LoggerFactory.getLogger(GateClientInBoundHandlerScript.class);
	
	@Override
	public void channelActive(Class<? extends ChannelHandler> handlerClass, Service<? extends BaseServerConfig> service,Channel channel) {
		if(!handlerClass.isAssignableFrom(DefaultClientInBoundHandler.class)
				||!(service instanceof MutilNettyTcpClientService)) {
			return;
		}
		
		//向网关服注册session
		
		IMutilTcpClientService<? extends BaseServerConfig> client=BydrServer.getInstance().getBydr2GateClient();
		if(!(client instanceof MutilNettyTcpClientService)) {
			logger.warn("未开启nettyfuw");
			return;
		}
		
		NettyClientConfig config=((MutilNettyTcpClientService)client).getNettyClientConfig();
		ServerRegisterRequest.Builder builder=ServerRegisterRequest.newBuilder();
		ServerMessage.ServerInfo.Builder info=ServerMessage.ServerInfo.newBuilder();
		info.setId(config.getId());
		info.setIp("");
		info.setMaxUserCount(1000);
		info.setOnline(1);
		info.setName(config.getName());
		info.setState(ServerState.NORMAL.getState());
		info.setType(config.getType().getType());
		info.setWwwip("");
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IGameServerCheckScript.class, 
				script ->script.buildServerInfo(info));
		builder.setServerInfo(info);
		channel.writeAndFlush(new IDMessage(channel, builder.build(), 0,0));
	}
}
