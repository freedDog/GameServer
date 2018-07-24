package com.jbm.game.model.handler.http.favicon;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.HttpHandler;

/**
 * 获取favicon.ico 图标
 *  * <p>谷歌浏览器地址栏请求会额外发送次消息，IE不发送</p>
 * <p>
 * @author JiangBangMing
 *
 * 2018年7月23日 下午1:49:31
 */

@HandlerEntity(path="/favicon.ico")
public class GetFaviconHandler extends HttpHandler{

	@Override
	public void run() {
		sendMsg("favicon.ico");
	}

}
