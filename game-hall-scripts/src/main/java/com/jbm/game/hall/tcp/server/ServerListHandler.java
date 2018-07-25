package com.jbm.game.hall.tcp.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.hall.server.HallServer;
import com.jbm.game.message.ServerMessage;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.ServerMessage.ServerListResponse;

/**
 * 返回服务器列表
 * @author JiangBangMing
 *
 * 2018年7月25日 下午8:23:29
 */
@HandlerEntity(mid=MID.ServerListRes_VALUE,msg=ServerListResponse.class)
public class ServerListHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(ServerListHandler.class);
	
	@Override
	public void run() {
		ServerListResponse res=getMsg();
		if(res==null) {
			return;
		}
		
		List<ServerMessage.ServerInfo> list=res.getServerInfoList();
		if(list==null) {
			logger.warn("没有可用网关服务器");
			return;
		}
		//更新服务器消息
		Set<Integer> serverIds=new HashSet<Integer>();
		list.forEach(info ->{
			HallServer.getInstance().getHall2GateClient().updateHallServerInfo(info);
			serverIds.add(info.getId());
		});
		Map<Integer, ServerInfo> hallServers=HallServer.getInstance().getHall2GateClient().getServers();
		
		if(hallServers.size()!=list.size()) {
			List<Integer> ids=new ArrayList<>(hallServers.keySet());
			ids.removeAll(serverIds);
			ids.forEach(serverId ->{
				HallServer.getInstance().getHall2GateClient().removeTcpClient(serverId);
			});
		}
	}
}
