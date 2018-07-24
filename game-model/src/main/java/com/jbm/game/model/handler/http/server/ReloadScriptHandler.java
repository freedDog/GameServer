package com.jbm.game.model.handler.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.HttpHandler;
import com.jbm.game.engine.mail.MailConfig;
import com.jbm.game.engine.mail.MailManager;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.util.MsgUtil;
import com.jbm.game.model.constant.Config;

/**
 * 加载脚本
 * @author JiangBangMing
 *
 * 2018年7月23日 下午2:45:51
 */

@HandlerEntity(path="/server/reloadScript")
public class ReloadScriptHandler  extends HttpHandler{
	
	private static final Logger logger=LoggerFactory.getLogger(ReloadScriptHandler.class);

	@Override
	public void run() {
		String auth=getString("auth");
		String scriptPath=getString("scriptPath");
		if(!Config.SERVER_AUTH.equals(auth)) {
			sendMsg("验证失败");
			return;
		}
		
		String loadClass=null;
		if(scriptPath==null) {
			loadClass=ScriptManager.getInstance().init(null);
		}else {
			if(scriptPath.contains(",")) {
				String[] split=scriptPath.split(",");
				loadClass=ScriptManager.getInstance().loadJava(split);
			}else {
				loadClass=ScriptManager.getInstance().loadJava(scriptPath);
			}
		}
		String info=String.format("%s 加载脚本:%s",MsgUtil.getIp(getSession()),loadClass);
		logger.info(info);
		sendMsg(info);
		MailConfig mailConfig = MailManager.getInstance().getMailConfig();
		String[] recives = mailConfig.getReciveUser().toArray(new String[mailConfig.getReciveUser().size()]);
		MailManager.getInstance().sendTextMail("加载脚本", Config.SERVER_NAME +"\r\n"+ info, recives);
	}

}
