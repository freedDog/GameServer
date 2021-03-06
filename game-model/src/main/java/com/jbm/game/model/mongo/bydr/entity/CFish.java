package com.jbm.game.model.mongo.bydr.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.jbm.game.model.mongo.IConfigChecker;

/**
 * 鱼配置信息
 * @author JiangBangMing
 *
 * 2018年7月23日 下午3:29:32
 */

@Entity(value="c_fish",noClassnameStored=true)
public class CFish implements IConfigChecker{

	@Id
	@Indexed
	private int id;
	
	//名称
	private String name;
	//类型
	private int type;
	
	private int baseSpeed;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBaseSpeed() {
		return baseSpeed;
	}

	public void setBaseSpeed(int baseSpeed) {
		this.baseSpeed = baseSpeed;
	}
	
	
}
