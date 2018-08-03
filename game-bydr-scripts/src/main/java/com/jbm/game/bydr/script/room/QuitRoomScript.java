package com.jbm.game.bydr.script.room;

import java.util.Iterator;

import com.jbm.game.bydr.script.IRoomScript;
import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.bydr.struct.room.Room;

/**
 * 退出房间
 * @author JiangBangMing
 *
 * 2018年8月3日 下午2:43:34
 */
public class QuitRoomScript implements IRoomScript{

	@Override
	public void quitRoom(Role role, Room room) {
		Iterator<Role> iterator=room.getRoles().values().iterator();
		while(iterator.hasNext()) {
			if(iterator.next().getId()==role.getId()) {
				iterator.remove();
			}
		}
	}
}
