package com.jbm.game.bydr.script.room;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.manager.RoomManager;
import com.jbm.game.bydr.script.IFishScript;
import com.jbm.game.bydr.struct.Fish;
import com.jbm.game.bydr.struct.room.Room;
import com.jbm.game.engine.math.MathUtil;

/**
 * 刷新房间鱼群
 * @author JiangBangMing
 *
 * 2018年8月3日 下午2:48:33
 */
public class RoomFishRefreshScript implements IFishScript{

	private static final Logger logger=LoggerFactory.getLogger(RoomFishRefreshScript.class);
	
	@Override
	public void fishRefresh(Room room) {
//		Room room=(Room) iRoom;
		//TODO 测试，每秒刷新一条鱼
		Fish fish1 = bornFish(room, MathUtil.random(1, 5), null);
		Fish fish2 = bornFish(room, MathUtil.random(1, 5), null);
		Fish fish3 = bornFish(room, MathUtil.random(1, 5), null);
		Fish fish4 = bornFish(room, MathUtil.random(1, 5), null);
		Fish fish5 = bornFish(room, MathUtil.random(1, 5), null);
		
		RoomManager.getInstance().broadcastFishEnter(room, fish1,fish2,fish3,fish4,fish5/*,fish1,fish2,fish3,fish4,fish5*/);
	}
	
	/**
	 * 出生
	 * @param room
	 * @param configId
	 * @param fishConsumer
	 * @return
	 */
	private Fish bornFish(Room room,int configId,Consumer<Fish> fishConsumer) {
		Fish fish=new Fish();
		fish.setConfigId(configId);
		
		if(fishConsumer!=null) {
			fishConsumer.accept(fish);
		}
		
		fish.setRoom(room);
		fish.setTrackIds(new ArrayList<>());
		room.getFishMap().put(fish.getId(), fish);
		return fish;
	}
}
