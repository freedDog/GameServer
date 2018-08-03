package com.jbm.game.bydr.script.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.script.IRoleScript;
import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.engine.redis.jedis.JedisManager;
import com.jbm.game.model.constant.Reason;
import com.jbm.game.model.redis.key.HallKey;

/**
 * 修改角色金币
 * @author JiangBangMing
 *
 * 2018年8月3日 下午2:11:07
 */
public class RoleGoldChangeScript implements IRoleScript{

	private static final Logger logger=LoggerFactory.getLogger(RoleGoldChangeScript.class);
	
	@Override
	public void changeGold(Role role, int add, Reason reason) {
		long gold=role.getGold()+add;
		if(gold<0||gold>Long.MAX_VALUE) {
			logger.warn("玩家更新金币异常，{}+{}={}",role.getGold(),add,gold);
			role.setGold(0);
			return;
		}
		role.setGold(gold);
		if(reason==Reason.RoleFire) {
			role.setWinGold(role.getWinGold()+add);
		}
	}
	
	@Override
	public void syncGold(Role role, Reason reason) {
		if(role.getWinGold()!=0) {
			String key=HallKey.Role_Map_Info.getKey(role.getId());
			
			long addAndGet=JedisManager.getJedisCluster().hincrBy(key, "gold", role.getWinGold());
			role.setWinGold(0);
			if(logger.isDebugEnabled()) {
				logger.debug("更新后金币为{}",addAndGet);
			}
		}
	}
}
