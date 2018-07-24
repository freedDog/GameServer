package com.jbm.game.model.mongo.bydr.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.jbm.game.model.mongo.IConfigChecker;

/**
 * 房间配置
 * @author JiangBangMing
 *
 * 2018年7月23日 下午3:31:53
 */

@Entity(value="c_room",noClassnameStored=true)
public class CRoom implements IConfigChecker{
	@Id
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
