package com.jbm.game.bydr.struct;

import java.util.Set;

import org.apache.mina.util.ConcurrentHashSet;

import com.alibaba.fastjson.annotation.JSONField;
import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.util.JsonUtil;
import com.jbm.game.model.redis.key.BydrKey;

/**
 * 队伍信息
 * @author JiangBangMing
 *
 * 2018年8月2日 下午1:54:46
 */
public class Team {
	/**唯一ID，用队长ID作为ID*/
	@JSONField(serialize=true)
	private long id;
	
	/**所在服务器ID，队长所在服务器*/
	@JSONField(serialize=true)
	private int serverId;
	
	/**类型，当前只有竞技场比赛*/
	@JSONField(serialize=true)
	private int type;
	
	/**级别*/
	@JSONField(serialize=true)
	private int rank;
	
	/**级别*/
	@JSONField(serialize=true)
	private Set<Long> roleIds=new ConcurrentHashSet<>();
	
	/**状态*/
	@JSONField(serialize=true)
	private int status;
	
	public Team(long id,int serverId,int rank){
		this.id=id;
		this.serverId=serverId;
		this.rank=rank;
		this.roleIds.add(id);
	}
	
	
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public int getServerId() {
		return serverId;
	}



	public void setServerId(int serverId) {
		this.serverId = serverId;
	}



	public int getType() {
		return type;
	}



	public void setType(int type) {
		this.type = type;
	}



	public int getRank() {
		return rank;
	}



	public void setRank(int rank) {
		this.rank = rank;
	}



	public Set<Long> getRoleIds() {
		return roleIds;
	}



	public void setRoleIds(Set<Long> roleIds) {
		this.roleIds = roleIds;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}



	public void saveToRedis(){
		JedisManager.getJedisCluster().hset(BydrKey.Team_Map.getKey(), String.valueOf(this.id), JsonUtil.toJSONString(this));
	}
}
