package com.jbm.game.model.handler.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.HttpHandler;
import com.jbm.game.engine.util.SysUtil;
import com.jbm.game.model.constant.Config;

/**
 * 获取线程信息
 * @author JiangBangMing
 *
 * 2018年7月23日 下午2:52:26
 */

@HandlerEntity(path="/server/thread/info")
public class ThreadInfoHandler extends HttpHandler{
	
	private static final Logger logger=LoggerFactory.getLogger(ThreadInfoHandler.class);
	@Override
	public void run() {
		String auth=getString("auth");
		if(!Config.SERVER_AUTH.equals(auth)) {
			sendMsg("验证失败");
			return;
		}
		String info=SysUtil.threadInfo("<br>");
		logger.info(info);
		info=info.trim().replaceAll("/n", "").replaceAll("/t", "&nbsp;&nbsp;");
		sendMsg(info);
		
	}

}
