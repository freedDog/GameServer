package com.jbm.game.bydr.script.room;

import java.time.LocalTime;

import com.jbm.game.bydr.script.IRoomScript;
import com.jbm.game.bydr.struct.room.Room;
import com.jbm.game.model.constant.Reason;

/**
 * 房间角色监测更新
 * @author JiangBangMing
 *
 * 2018年8月3日 下午2:54:58
 */
public class RoomRoleUpdateScript implements IRoomScript{

	@Override
	public void secondHandler(Room room, LocalTime localTime) {
		IRoomScript.super.secondHandler(room, localTime);
	}
	
	@Override
	public void minuteHandler(Room room, LocalTime localTime) {
		room.getRoles().forEach((seat,role) ->{
			//更新金币
			if(role.getWinGold()!=0) {
				role.syncGold(Reason.RoleSync);
			}
		});
	}
}
