package com.jbm.game.bydr.struct;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jbm.game.bydr.struct.room.Room;
import com.jbm.game.engine.cache.IMemoryObject;
import com.jbm.game.model.constant.Config;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年8月2日 下午1:11:16
 */
public class Fish implements IMemoryObject,Serializable{
	
	private static final long serialVersionUID = 474405886633007247L;
	private long id;
	// 配置ID
	private int configId; 
	// 出生时间
	private long bornTime; 
	// 当前速度
	private int speed; 
	private long speedEndTime; // 当前速度结束时间
	private long dieTime; 	// 死亡时间
	private List<Integer> trackIds; // 鱼游动路线ID
	private int refreshId; // 刷新ID
	private long topSpeedStartTime; // 极速开始时间
	private int topSpeed; // 极速
	private int formationId;	//阵型ID
	
	
	private transient Room room;	//所属的房间

	public Fish() {
		this.id = Config.getId();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public long getBornTime() {
		return bornTime;
	}

	public void setBornTime(long bornTime) {
		this.bornTime = bornTime;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public long getSpeedEndTime() {
		return speedEndTime;
	}

	public void setSpeedEndTime(long speedEndTime) {
		this.speedEndTime = speedEndTime;
	}

	public long getDieTime() {
		return dieTime;
	}

	public void setDieTime(long dieTime) {
		this.dieTime = dieTime;
	}

	public int getRefreshId() {
		return refreshId;
	}

	public void setRefreshId(int refreshId) {
		this.refreshId = refreshId;
	}

	public List<Integer> getTrackIds() {
		return trackIds;
	}

	public void setTrackIds(List<Integer> trackIds) {
		this.trackIds = trackIds;
	}

	public long getTopSpeedStartTime() {
		return topSpeedStartTime;
	}

	public void setTopSpeedStartTime(long topSpeedStartTime) {
		this.topSpeedStartTime = topSpeedStartTime;
	}

	public int getTopSpeed() {
		return topSpeed;
	}

	public void setTopSpeed(int topSpeed) {
		this.topSpeed = topSpeed;
	}

	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public int getFormationId() {
		return formationId;
	}

	public void setFormationId(int formationId) {
		this.formationId = formationId;
	}

//	/**
//	 * 复制鱼
//	 * 
//	 * @return
//	 */
//	public Fish copy() {
//		Fish fish = FishManager.getInstance().getFishPool().get(Fish.class);
//		fish.id = Config.getId();
//		fish.bornTime = this.bornTime;
//		fish.configId = this.configId;
//		fish.dieTime = this.dieTime;
//		fish.refreshId = this.refreshId;
//		fish.speed = this.speed;
//		fish.speedEndTime = this.speedEndTime;
//		fish.topSpeed = this.topSpeed;
//		fish.topSpeedStartTime = this.topSpeedStartTime;
//		fish.trackIds = new ArrayList<Integer>(this.trackIds);
//		fish.room=this.room;
//		fish.formationId=this.formationId;
//		return fish;
//	}

	@Override
	public void reset() {
		this.id = Config.getId();
		this.bornTime = 0;
		this.configId = 0;
		this.dieTime = 0;
		this.refreshId = 0;
		this.speed = 0;
		this.speedEndTime = 0;
		this.topSpeed = 0;
		this.topSpeedStartTime = 0;
		this.trackIds.clear();
		this.formationId=0;
		this.room=null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fish other = (Fish) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

//	public FishType getFishType() {
//		ConfigFish configFish = ConfigManager.getInstance().getConfigFish(configId);
//		return FishType.valueOf(configFish.getType());
//	}
}
