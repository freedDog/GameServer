package com.jbm.game.bydr.script;

import com.jbm.game.engine.handler.HttpHandler;
import com.jbm.game.engine.script.IScript;

/**
 * gm
 * @author JiangBangMing
 *
 * 2018年8月2日 下午1:27:44
 */
public interface IGmScript extends IScript{

	/**
	 * 验证http请求sid
	 * @param handler
	 * @return
	 */
	public default boolean authHttpSid(HttpHandler handler) {
		return false;
	}
}
