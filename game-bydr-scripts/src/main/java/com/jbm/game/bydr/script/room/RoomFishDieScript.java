package com.jbm.game.bydr.script.room;

import java.time.LocalTime;

import com.jbm.game.bydr.script.IRoomScript;
import com.jbm.game.bydr.struct.room.Room;

/**
 * 定时清理房间死亡的鱼
 * @author JiangBangMing
 *
 * 2018年8月3日 下午2:46:12
 */
public class RoomFishDieScript implements IRoomScript{

	@Override
	public void secondHandler(Room room, LocalTime localTime) {
		//每隔三秒清理一次
		if(localTime.getSecond()%3!=0) {
			return;
		}
		if(room.getFishMap().size()>1000) {
			room.getFishMap().clear();
		}
	}
}
