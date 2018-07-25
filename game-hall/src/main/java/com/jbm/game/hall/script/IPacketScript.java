package com.jbm.game.hall.script;

import java.util.function.Consumer;

import com.jbm.game.engine.script.IScript;
import com.jbm.game.model.constant.Reason;
import com.jbm.game.model.struct.Item;
import com.jbm.game.model.struct.Role;

/**
 * 道具
 * @author JiangBangMing
 *
 * 2018年7月25日 下午3:30:46
 */
public interface IPacketScript extends IScript{

	/**
	 * 使用道具
	 * @param role
	 * @param id 道具id
	 * @param num 数量
	 * @param reason
	 * @param itemConsumer
	 */
	public default void useItem(Role role,long id,int num,Reason reason,Consumer<Item> itemConsumer) {
		
	}
	
	/**
	 * 添加道具
	 * @param role
	 * @param configId
	 * @param num
	 * @param reason
	 * @param itemConsumer
	 * @return
	 */
	public default Item addItem(Role role,int configId,int num,Reason reason,Consumer<Item> itemConsumer) {
		return null;
	}
}
