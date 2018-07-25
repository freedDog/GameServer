package com.jbm.game.model.struct;

import org.mongodb.morphia.annotations.Entity;

import com.jbm.game.engine.struct.Person;
import com.jbm.game.model.redis.key.HallKey;

/**
 * 玩家角色实体
 * @author JiangBangMing
 *
 * 2018年7月23日 下午3:56:32
 */

@Entity(value="role",noClassnameStored=true)
public class Role  extends Person{
	
	
	
	
	public String getRoleRedisKey() {
		return HallKey.Role_Map_Info.getKey(this.id);
	}

}
