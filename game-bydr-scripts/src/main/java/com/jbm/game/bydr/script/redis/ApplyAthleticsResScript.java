package com.jbm.game.bydr.script.redis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.manager.ConfigManager;
import com.jbm.game.bydr.manager.RoleManager;
import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.engine.redis.IPubSubScript;
import com.jbm.game.engine.redis.jedis.JedisPubSubMessage;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.message.ServerMessage.ChangeRoleServerRequest;
import com.jbm.game.message.bydr.BydrRoomMessage.ApplyAthleticsResponse;
import com.jbm.game.model.redis.channel.BydrChannel;

/**
 * 报名竞技赛结果
 * <p>广播人数变化 人数满后，进行跨服处理</p>
 * @author JiangBangMing
 *
 * 2018年8月3日 下午1:50:35
 */
public class ApplyAthleticsResScript implements IPubSubScript{

	private static final Logger logger=LoggerFactory.getLogger(ApplyAthleticsResScript.class);
	
	@Override
	public void onMessage(String channle, JedisPubSubMessage message) {
		if(!BydrChannel.ApplyAthleticsRes.name().equals(channle)) {
			return;
		}
		logger.info("竞技赛");
		List<Role> roles=new ArrayList<>();
		message.getIds().forEach(roleId ->{
			Role role=RoleManager.getInstance().getRole(roleId);
			if(role!=null) {
				roles.add(role);
			}
		});
		if(roles.size()<1) {
			logger.warn("不是该服务器跨服角色");
			return;
		}
		//广播玩家
		ApplyAthleticsResponse.Builder builder=ApplyAthleticsResponse.newBuilder();
		builder.addAllRoleId(message.getIds());
		ApplyAthleticsResponse response=builder.build();
		roles.forEach( role -> role.sendMsg(response));
		
		//进行跨服处理
		if(message.getIds().size()>=ConfigManager.getInstance().getGameConfig().getRoomSize()) {
			int targetServerId=message.getServer();//目标服务器ID
			ChangeRoleServerRequest.Builder serverBuilder=ChangeRoleServerRequest.newBuilder();
			roles.forEach( role ->{
				if(role.getGameId()!=targetServerId) {
					//通知网关服，改变session
					serverBuilder.clear();
					serverBuilder.setRoleId(role.getId());
					serverBuilder.setServerId(targetServerId);
					serverBuilder.setServerType(ServerType.GAME_BYDR.getType());
					role.sendMsg(serverBuilder.build());
					logger.info("角色{}跨服",role.getId());
				}
			});
		}
	}

}
