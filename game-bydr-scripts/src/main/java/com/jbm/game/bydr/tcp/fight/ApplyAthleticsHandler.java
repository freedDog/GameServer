package com.jbm.game.bydr.tcp.fight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.redis.jedis.JedisPubSubMessage;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.bydr.BydrRoomMessage.ApplyAthleticsRequest;
import com.jbm.game.model.redis.channel.BydrWorldChannel;

/**
 * 报名竞技赛
 * @author JiangBangMing
 *
 * 2018年8月3日 下午3:31:19
 */

@HandlerEntity(mid=MID.ApplyAthleticsReq_VALUE,msg=ApplyAthleticsRequest.class)
public class ApplyAthleticsHandler extends TcpHandler{

	private static final Logger logger=LoggerFactory.getLogger(ApplyAthleticsHandler.class);
	
	@Override
	public void run() {
		ApplyAthleticsRequest req=getMsg();
		
		JedisPubSubMessage msg=new JedisPubSubMessage(rid,req.getType().getNumber(),req.getRank());
		JedisManager.getJedisCluster().publish(BydrWorldChannel.ApplyAthleticsReq.toString(), msg.toString());
		logger.info("{} 参加竞技赛",rid);
	}
}
