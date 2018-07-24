package com.jbm.game.gate.script.server;

import java.util.HashSet;
import java.util.Set;

import com.jbm.game.engine.script.IInitScript;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.gate.script.IGateServerScript;
import com.jbm.game.message.Mid.MID;

/**
 * 注册udp 消息和udp 服务器
 * @author JiangBangMing
 *
 * 2018年7月24日 下午6:06:10
 */
public class UdpMsgRegisterScript implements IGateServerScript,IInitScript{

	private Set<Integer> udpMsgId=new HashSet<>();//udp支持的消息
	private Set<ServerType> udpServers=new HashSet<>();//udp
	
	@Override
	public void init() {
		//注册udp 游戏
		udpServers.add(ServerType.GAME_BYDR);
		
		//注册udp消息，只需要注册返回消息
		udpMsgId.add(MID.HeartRes_VALUE);
		udpMsgId.add(MID.EnterRoomRes_VALUE);
		udpMsgId.add(MID.ChatRes_VALUE);
	}
	
	@Override
	public boolean isUdpMsg(ServerType serverType, int msgId) {
		if(serverType==null) {
			return false;
		}
		return udpServers.contains(serverType)&&udpMsgId.contains(msgId);
	}

}
