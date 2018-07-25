package com.jbm.game.hall.script.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.mq.IMQScript;

/**
 * MQ 消息接收
 * @author JiangBangMing
 *
 * 2018年7月25日 下午5:37:58
 */
public class MQMsgReceiveScript implements IMQScript{

	private static final Logger logger=LoggerFactory.getLogger(MQMsgReceiveScript.class);
	 @Override
	public void onMessage(String msg) {
		 logger.info(msg);
	 }
	
}
