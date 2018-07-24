package com.jbm.game.model.handler.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.HttpHandler;
import com.jbm.game.engine.mail.MailConfig;
import com.jbm.game.engine.mail.MailManager;
import com.jbm.game.engine.util.MsgUtil;
import com.jbm.game.model.constant.Config;

/**
 * 关闭服务器
 * @author JiangBangMing
 *
 * 2018年7月23日 下午1:51:02
 */

@HandlerEntity(path="/server/exit")
public class ExitServerHandler extends HttpHandler{

	private static final Logger logger=LoggerFactory.getLogger(ExitServerHandler.class);

	@Override
	public void run() {
		String auth=getString("auth");
		if(!Config.SERVER_AUTH.equals(auth)) {
			sendMsg("验证失败");
			return;
		}
		
		String info=String.format("%s 关闭服务器",MsgUtil.getIp(getSession()));
		logger.info(info);
		sendMsg(info);
		MailConfig mailConfig=MailManager.getInstance().getMailConfig();
		String[] recives=mailConfig.getReciveUser().toArray(new String[mailConfig.getReciveUser().size()]);
		MailManager.getInstance().sendTextMailAsync("服务器关闭", info, recives);
		System.exit(1);
	}
}
