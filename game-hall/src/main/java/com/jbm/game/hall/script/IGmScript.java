package com.jbm.game.hall.script;

import com.jbm.game.engine.script.IScript;

/**
 * gm脚本
 * @author JiangBangMing
 *
 * 2018年7月25日 下午3:24:08
 */
public interface IGmScript extends IScript{

	public default String executeGm(long roleId,String gmCmd) {
		return String.format("Gm {} 未执行", gmCmd);
	}
	
	/**
	 * 是否为gm命令
	 * @param gmCmd
	 * @return
	 */
	public default boolean isGMCmd(String gmCmd) {
		return gmCmd!=null&&gmCmd.startsWith("&");
	}
}
