package com.jbm.game.bydr.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.manager.ConfigManager;
import com.jbm.game.bydr.manager.RoleManager;
import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.bydr.struct.room.Room;
import com.jbm.game.bydr.thread.timer.RoomTimer;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.thread.ExecutorPool;
import com.jbm.game.engine.thread.ServerThread;

/**
 * 逻辑执行线程池，将玩家的逻辑操作分配到同一个线程中执行，避免并发数据异常
 * @author JiangBangMing
 *
 * 2018年8月2日 下午4:50:24
 */
public class RoomExecutor extends ExecutorPool{

	private static final Logger logger=LoggerFactory.getLogger(RoomExecutor.class);
	private static final ThreadGroup threadGroup=new ThreadGroup("房间线程组");
	
	//key:房间ID
	private Map<Long, RoomThread> roomThreads=new HashMap<>();
	//房间线程
	private List<RoomThread> threads=Collections.synchronizedList(new ArrayList<>());
	
	@Override
	public void execute(Runnable command) {
		if(command instanceof TcpHandler) {
			TcpHandler handler=(TcpHandler)command;
			Role role=RoleManager.getInstance().getRole(handler.getRid());
			if(role==null) {
				logger.warn("角色{} 未在线",handler.getRid());
				return;
			}
			handler.setPerson(role);
			ServerThread serverThread=roomThreads.get(role.getRoomId());
			if(serverThread==null) {
				logger.warn("房间{}已经销毁",role.getRoomId());
				return;
			}
			serverThread.execute(handler);
		}
	}
	
	/**
	 * 添加逻辑线程
	 * @param room
	 * @return
	 */
	public RoomThread addRoom(Room room) {
		if(roomThreads.containsKey(room.getId())) {
			logger.warn("房间{}已在线程中",room.getId());
			return roomThreads.get(room.getId());
		}
		
		Optional<RoomThread> findAny=threads.stream().filter(
				thread ->thread.getRooms().size()<ConfigManager.getInstance().getGameConfig().getThradRoomNum())
				.findAny();
		RoomThread roomThread=null;
		if(findAny.isPresent()) {
			roomThread=findAny.get();
			roomThread.getRooms().add(room);
			roomThread.getRoomTimer().addRoom(room);
		}else {
			roomThread=new RoomThread(threadGroup, room);
			threads.add(roomThread);
			roomThread.start();
			
			RoomTimer roomFishTimer=new RoomTimer(room, roomThread);
			roomThread.setRoomTimer(roomFishTimer);
			roomThread.addTimerEvent(roomFishTimer);
		}
		room.setRoomThread(roomThread);
		roomThreads.put(room.getId(), roomThread);
		
		return roomThread;
	}
	
	/**
	 * 移除线程
	 * @param room
	 * @return
	 */
	public RoomThread removeRoom(Room room) {
		RoomThread roomThread=roomThreads.remove(room.getId());
		roomThread.getRooms().remove(room);
		roomThread.getRoomTimer().removeRoom(room);
		return roomThread;
	}
	
	@Override
	public void stop() {
		threads.forEach(thread -> thread.stop(true));
	}
	
	public RoomThread getRoomThread(long roomId) {
		return roomThreads.get(roomId);
	}
}
