package com.jbm.game.model.mongo.bydr.entity;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.jbm.game.model.mongo.IConfigChecker;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年7月23日 下午3:33:21
 */
@Entity(value="config_fish_boom",noClassnameStored=true)
public class ConfigFishBoom implements IConfigChecker{
	
	@Id
	@Indexed
	private int id;
	//
	private List formationIds;
	
	private List refreshTimes;
	
	private List lineIds;
	
	private List survieTimes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List getFormationIds() {
		return formationIds;
	}

	public void setFormationIds(List formationIds) {
		this.formationIds = formationIds;
	}

	public List getRefreshTimes() {
		return refreshTimes;
	}

	public void setRefreshTimes(List refreshTimes) {
		this.refreshTimes = refreshTimes;
	}

	public List getLineIds() {
		return lineIds;
	}

	public void setLineIds(List lineIds) {
		this.lineIds = lineIds;
	}

	public List getSurvieTimes() {
		return survieTimes;
	}

	public void setSurvieTimes(List survieTimes) {
		this.survieTimes = survieTimes;
	}
	
	
}
