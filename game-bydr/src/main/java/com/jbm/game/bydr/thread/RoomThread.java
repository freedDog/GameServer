package com.jbm.game.bydr.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.jbm.game.bydr.struct.room.Room;
import com.jbm.game.bydr.thread.timer.RoomTimer;
import com.jbm.game.engine.thread.ServerThread;
import com.jbm.game.engine.thread.ThreadType;
import com.jbm.game.engine.thread.timer.TimerEvent;

/**
 * 房间逻辑线程
  * <p>
 * 一个线程处理多个房间
 * </p>
 * @author JiangBangMing
 *
 * 2018年8月2日 下午1:52:31
 */
public class RoomThread extends ServerThread{

	private static final AtomicInteger threadNum=new AtomicInteger(0);
	private List<Room> rooms=new ArrayList<>();
	private RoomTimer roomTimer;
	
	public RoomThread(ThreadGroup threadGroup,Room room) {
		super(threadGroup, ThreadType.ROOM.toString()+"-"+threadNum.getAndIncrement(), 500, 10000);
		this.rooms.add(room);
	}
	
	
	public RoomTimer getRoomTimer() {
		return roomTimer;
	}

	public void setRoomTimer(RoomTimer roomTimer) {
		this.roomTimer = roomTimer;
	}

	public List<Room> getRooms() {
		return rooms;
	}
	
	public Room getRoom(long roomId) {
		Optional<Room> findAny=this.rooms.stream().filter(r -> r.getId()==roomId).findAny();
		if(findAny.isPresent()) {
			return findAny.get();
		}
		return null;
	}

	
}
