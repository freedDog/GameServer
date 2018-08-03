package com.jbm.game.bydr.script;

import java.time.LocalTime;

import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.bydr.struct.room.Room;
import com.jbm.game.engine.script.IScript;
import com.jbm.game.message.bydr.BydrRoomMessage;

/**
 * 房间脚本
 * @author JiangBangMing
 *
 * 2018年8月2日 下午1:34:53
 */
public interface IRoomScript extends IScript{

	
	/**
	 * 进入房间
	 * @param role
	 * @param room
	 */
	public default void enterRoom(Role role,Room room) {
		
	}
	
	/**
	 * 进入房间
	 * @param role
	 * @param roomType 房间类型
	 * @param rank 级别
	 */
	public default void enterRoom(Role role,BydrRoomMessage.RoomType roomType,int rank) {
		
	}
	
	/**
	 * 退出房间
	 * @param role
	 * @param room
	 */
	public default void quitRoom(Role role,Room room) {
		
	}
	
	/**
	 * 跑马灯
	 * <p>
	 * 没有辛运星
	 * </p>
	 * @param role
	 * @param totalGold
	 * @param accumulateGold
	 * @param multiple
	 * @param fishName
	 */
	public default void sendPmd(Role role,int totalGold,int accumulateGold, int multiple, String fishName) {
		
	}
	/**
	 * 销毁房间
	 * @param iRoom
	 */
	public default void destoryRoom(Room iRoom) {
		
	}
	
	/**
	 * 每秒执行
	 * @param room
	 * @param localTime
	 */
	public default void secondHandler(Room room,LocalTime localTime) {
		
	}
	/**
	 * 每分钟执行
	 * @param room
	 * @param localTime
	 */
	public default void minuteHandler(Room room,LocalTime localTime) {
		
	}
	
	/**
	 * 每小时执行
	 * @param room
	 * @param localTime
	 */
	public default void hourHandler(Room room,LocalTime localTime) {
		
	}
	
}
