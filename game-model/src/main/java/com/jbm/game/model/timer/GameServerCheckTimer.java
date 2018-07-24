package com.jbm.game.model.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.engine.mina.config.MinaClientConfig;
import com.jbm.game.engine.netty.config.NettyClientConfig;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.server.BaseServerConfig;
import com.jbm.game.engine.server.IMutilTcpClientService;
import com.jbm.game.engine.server.ITcpClientService;
import com.jbm.game.engine.server.ServerState;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.engine.thread.timer.ScheduledTask;
import com.jbm.game.engine.util.SysUtil;
import com.jbm.game.message.ServerMessage.ServerInfo;
import com.jbm.game.message.ServerMessage.ServerListRequest;
import com.jbm.game.message.ServerMessage.ServerRegisterRequest;
import com.jbm.game.message.ServerMessage.ServerRegisterRequest.Builder;
import com.jbm.game.model.script.IGameServerCheckScript;

/**
 * 游戏服务器 状态监测，重连线程
 * <p>
 * 每隔10秒监测一次
 * @author JiangBangMing
 *
 * 2018年7月24日 下午3:38:18
 */
public class GameServerCheckTimer extends ScheduledTask{

	
	private static final Logger logger=LoggerFactory.getLogger(GameServerCheckTimer.class);
	private ITcpClientService<? extends BaseServerConfig> clusterService;//集群连接服务
	private IMutilTcpClientService<? extends BaseServerConfig> gateService;//网关连接服务
	private BaseServerConfig config;//游戏配置
	
	
	public GameServerCheckTimer(ITcpClientService<? extends BaseServerConfig> clusterSevice,
			IMutilTcpClientService<? extends BaseServerConfig> gateServic,BaseServerConfig config) {
		super(10000);
		this.clusterService=clusterSevice;
		this.gateService=gateServic;
		this.config=config;
	}
	@Override
	protected void executeTask() {
		//向网关和集群注册游戏服务器信息
		Builder registerRequestBuilder=builderServerReqisterRequit(this.config);
		ServerRegisterRequest serverRegisterRequest=registerRequestBuilder.build();
		
		//集群服
		clusterService.sendMsg(serverRegisterRequest);
		clusterService.checkStatus();
		
		//网关服，监测连接其他服务器客户端状态
		if(gateService!=null) {
			if(!gateService.broadcastMsg(serverRegisterRequest)) {
				logger.warn("大厅服未连接");
			}
			gateService.checkStatus();
		}
		
		//获取可连接的网络列表
		ServerListRequest.Builder builder=ServerListRequest.newBuilder();
		builder.setServerType(ServerType.GATE.getType());
		clusterService.sendMsg(builder.build());
	}
	
	/**
	 * 构建服务器更新注册消息
	 * @param baseServerConfig
	 * @return
	 */
	private ServerRegisterRequest.Builder builderServerReqisterRequit(BaseServerConfig baseServerConfig){
		ServerRegisterRequest.Builder builder=ServerRegisterRequest.newBuilder();
		ServerInfo.Builder info=ServerInfo.newBuilder();
		info.setId(baseServerConfig.getId());
		info.setIp("");
		info.setMaxUserCount(1000);
		info.setName(baseServerConfig.getName());
		info.setState(ServerState.NORMAL.getState());
		info.setWwwip("");
		info.setVersion(baseServerConfig.getVersion());
		info.setTotalMemory(SysUtil.totalMemory());
		info.setFreeMemory(SysUtil.freeMemory());
		if(baseServerConfig instanceof MinaClientConfig) {
			MinaClientConfig minaClientConfig=(MinaClientConfig)baseServerConfig;
			info.setType(minaClientConfig.getType().getType());
		}else if(baseServerConfig instanceof NettyClientConfig) {
			NettyClientConfig nettyClientConfig=(NettyClientConfig)baseServerConfig;
			info.setType(nettyClientConfig.getType().getType());
		}else {
			throw new RuntimeException("服务器配置未实现");
		}
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IGameServerCheckScript.class,
				script -> script.buildServerInfo(info));
		builder.setServerInfo(info);
		return builder;
		
	}
}
