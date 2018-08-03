package com.jbm.game.bydr.script.room;

import com.jbm.game.bydr.script.IRoomScript;
import com.jbm.game.bydr.struct.room.Room;

/**
 * 房间销毁
 * @author JiangBangMing
 *
 * 2018年8月3日 下午2:45:32
 */
public class RoomDestoryScript implements IRoomScript{

	@Override
	public void destoryRoom(Room iRoom) {
		IRoomScript.super.destoryRoom(iRoom);
	}
}
