package com.jbm.game.model.struct;

import java.lang.reflect.Method;
import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.annotation.JSONField;
import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.engine.struct.Person;
import com.jbm.game.engine.util.JsonUtil;
import com.jbm.game.engine.util.ReflectUtil;
import com.jbm.game.model.redis.key.HallKey;

/**
 * 玩家角色实体
 * @author JiangBangMing
 *
 * 2018年7月23日 下午3:56:32
 */

@Entity(value="role",noClassnameStored=true)
public class Role  extends Person{
	
	private static final transient Logger logger=LoggerFactory.getLogger(Role.class);
	
	/**setter 方法集合*/
	@JSONField(serialize=false)
	protected transient static final Map<String, Method> WRITEMTHODS=ReflectUtil.getReadMethod(Role.class);
	//所咋游戏服类型
	@JSONField
	private int gameTeyp;

	/**
	 * 存储玩家基本属性到redis
	 * @param propertiesName
	 */
	public void saveToRedis(String propertiesName) {
		if(this.id<1) {
			return;
		}
		String key=HallKey.Role_Map_Info.getKey(this.id);
		Method method=WRITEMTHODS.get(propertiesName);
		if(method!=null) {
			try {
				Object value=method.invoke(this);
				if(value!=null) {
					//使用redisson
					JedisManager.getJedisCluster().hset(key, propertiesName, value.toString());
				}else {
					logger.warn("属性{} 值为null",propertiesName);
				}
			}catch (Exception e) {
				logger.error("属性存储",e);
			}
		}else {
			logger.warn("属性:{} 未找到对应方法",propertiesName);
		}
	}
	
	/**
	 * 道具数量
	 * @return
	 */
	public long getItemCount() {
		String key=HallKey.Role_Map_Packet.getKey(this.id);
		return JedisManager.getJedisCluster().hlen(key);
	}
	
	/**
	 * 获取道具
	 * @param itemdId
	 * @return
	 */
	public Item getItem(long itemId) {
		String key=HallKey.Role_Map_Packet.getKey(this.id);
		return JedisManager.getInstance().hget(key, itemId, Item.class);
	}
	
	/**
	 * 所有道具
	 * @return
	 */
	public Map<Long, Item> getItems(){
		String key=HallKey.Role_Map_Packet.getKey(this.id);
		return JedisManager.getInstance().hgetAll(key,Long.class,Item.class);
	}
	/**
	 * 角色存 redis key
	 * @return
	 */
	public String getRoleRedisKey() {
		return HallKey.Role_Map_Info.getKey(this.id);
	}
	/**
	 * 存储整个role 对象
	 */
	public void saveToRedis() {
		JedisManager.getJedisCluster().hmset(getRoleRedisKey(), JsonUtil.object2Map(this));
	}
	
	public int getGameTeyp() {
		return gameTeyp;
	}



	public void setGameTeyp(int gameTeyp) {
		this.gameTeyp = gameTeyp;
	}




	

	

	
	


}
