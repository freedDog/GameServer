package com.jbm.game.bydr.script.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.manager.RoleManager;
import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.engine.redis.IPubSubScript;
import com.jbm.game.engine.redis.jedis.JedisPubSubMessage;
import com.jbm.game.model.constant.Config;
import com.jbm.game.model.constant.Reason;
import com.jbm.game.model.redis.channel.BydrChannel;

/**
 * 大厅通知角色数据改变
 * @author JiangBangMing
 *
 * 2018年8月3日 下午1:45:06
 */
public class HallRoleDataChangeScript implements IPubSubScript{

	private static final Logger logger=LoggerFactory.getLogger(HallRoleDataChangeScript.class);
	
	@Override
	public void onMessage(String channle, JedisPubSubMessage message) {
		if(!channle.startsWith("Hall")) {//channel 必须是以Hall开头
			return;
		}
		if(message.getServer()!=Config.SERVER_ID) {
			return;
		}
		
		Role role=RoleManager.getInstance().getRole(message.getId());
		if(role==null) {
			logger.warn("角色[{}] 已退出游戏:{}  {} 更新失败",message.getId(),channle,message.toString());
			return;
		}
		
		switch (BydrChannel.valueOf(channle)) {
		case HallGoldChange:
			role.changeGold(message.getTarget(), Reason.HallGoldChange);
			break;

		default:
			break;
		}
	}

}
