package com.jbm.game.bydr.script;

import com.jbm.game.bydr.struct.room.Room;
import com.jbm.game.engine.script.IScript;

/**
 * 鱼脚本
 * @author JiangBangMing
 *
 * 2018年8月2日 下午1:22:44
 */
public interface IFishScript extends IScript{

	/**
	 * 刷新
	 * @param room
	 */
	public default void fishRefresh(Room room) {
		
	}
	
	/**
	 * 死亡
	 * @param room
	 */
	public default  void fishDie(Room room) {
		
	}
}
