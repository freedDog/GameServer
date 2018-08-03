package com.jbm.game.bydr.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.script.IRoomScript;
import com.jbm.game.bydr.struct.Fish;
import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.bydr.struct.room.ClassicsRoom;
import com.jbm.game.bydr.struct.room.Room;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.message.bydr.BydrRoomMessage.FishEnterRoomResponse;
import com.jbm.game.message.bydr.BydrRoomMessage.FishInfo;
import com.jbm.game.message.bydr.BydrRoomMessage.RoomInfo;
import com.jbm.game.message.bydr.BydrRoomMessage.RoomRoleInfo;
import com.jbm.game.message.bydr.BydrRoomMessage.RoomType;

/**
 * 房间管理类
 * @author JiangBangMing
 *
 * 2018年8月2日 下午5:27:16
 */
public class RoomManager {

	private static final Logger logger=LoggerFactory.getLogger(RoomManager.class);
	private static volatile RoomManager roomManager;
	private Map<Long, Room> rooms=new ConcurrentHashMap<>();
	private Map<RoomType, List<Room>> roomTypes=new ConcurrentHashMap<>();
	
	private RoomManager() {
		
	}
	
	public static RoomManager getInstance() {
		if(roomManager==null) {
			synchronized (RoomManager.class) {
				if(roomManager==null) {
					roomManager=new RoomManager();
				}
			}
		}
		return roomManager;
	}
	
	/**
	 * 进入房间
	 * @param role
	 * @param roomType
	 * @param rank
	 */
	public void enterRoom(Role role,RoomType roomType,int rank) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoomScript.class, script -> script.enterRoom(role, roomType,rank));
	}
	
	/**
	 * 退出房间
	 * @param role
	 * @param roomId
	 */
	public void quitRoom(Role role,long roomId) {
		Room room=getRoom(roomId);
		if(room==null) {
			return;
		}
		Thread currentThread=Thread.currentThread();
		if(currentThread.equals(room.getRoomThread())) {
			ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoomScript.class, script->script.quitRoom(role, room));
		}else {
			room.getRoomThread().execute(() ->ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoomScript.class, script -> script.quitRoom(role, room)));
		}
	}
	
	/**
	 * 添加房间
	 * @param room
	 */
	public void addRoom(Room room) {
		rooms.put(room.getId(), room);
		if(room instanceof ClassicsRoom) {
			if(!roomTypes.containsKey(RoomType.CLASSICS)) {
				roomTypes.put(RoomType.CLASSICS, new ArrayList<>());
			}
			roomTypes.get(RoomType.CLASSICS).add(room);
		}
	}
	/**
	 * 获取房间
	 * @param roomId
	 * @return
	 */
	public Room getRoom(long roomId) {
		return rooms.get(roomId);
	}
	
	/**
	 * 获取房间列表
	 * @param roomType
	 * @return
	 */
	public List<Room> getRooms(RoomType roomType){
		if(roomTypes.containsKey(roomType)) {
			return roomTypes.get(roomType);
		}
		List<Room> list=new ArrayList<>();
		roomTypes.put(roomType, list);
		return list;
	}
	
	public void broadcastFishEnter(Room room,Fish... fishs) {
		if(fishs==null) {
			return;
		}
		FishEnterRoomResponse.Builder buidler=FishEnterRoomResponse.newBuilder();
		FishInfo.Builder fishInfo=FishInfo.newBuilder();
		for(Fish fish:fishs) {
			buidler.addFishInfo(buildFishInfo(fishInfo, fish));
		}
		
		FishEnterRoomResponse response=buidler.build();
		room.getRoles().values().forEach( r->{
			r.sendMsg(response);
		});
	}
	
	/**
	 * 构建玩家信息
	 * @param role
	 * @return
	 */
	public RoomRoleInfo buildRoomRoleInfo(Role role) {
		RoomRoleInfo.Builder builder=RoomRoleInfo.newBuilder();
		builder.setDiamond(role.getGem());
		builder.setGold(role.getGold());
		builder.setIcon(role.getHead()==null?"":role.getHead());
		builder.setNick(role.getNick()==null?"":role.getNick());
		builder.setLevel(role.getLevel());
		builder.setRid(role.getId());
		builder.setVip(role.isIs_vip());
		return builder.build();
	}
	
	/**
	 * 构建房间信息
	 * @param room
	 * @return
	 */
	public RoomInfo buildRoomInfo(Room room) {
		RoomInfo.Builder builder=RoomInfo.newBuilder();
		builder.setId(room.getId());
		builder.setType(room.getType());
		builder.setState(room.getState());
		return builder.build();
	}
	
	/**
	 * 构建鱼信息
	 * @param builder
	 * @param fish
	 * @return
	 */
	private FishInfo buildFishInfo(FishInfo.Builder builder,Fish fish) {
		builder.clear();
		builder.addId(fish.getId());
		builder.addConfigId(fish.getConfigId());
		builder.addAllTrackId(fish.getTrackIds());
		builder.setFormation(fish.getFormationId());//0 普通鱼  1 boss 鱼群   
		
		builder.setBornTime(fish.getBornTime());
		builder.setServerTime(System.currentTimeMillis());
		builder.setSpeed(fish.getSpeed());
		builder.setTopSpeed(fish.getTopSpeed());
		builder.setTopSpeedStartTime(fish.getTopSpeedStartTime());
		return builder.build();
	}
}
