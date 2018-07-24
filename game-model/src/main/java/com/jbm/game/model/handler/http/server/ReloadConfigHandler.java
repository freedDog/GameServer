package com.jbm.game.model.handler.http.server;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.HttpHandler;
import com.jbm.game.engine.mail.MailConfig;
import com.jbm.game.engine.mail.MailManager;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.engine.util.MsgUtil;
import com.jbm.game.engine.util.SymbolUtil;
import com.jbm.game.model.constant.Config;
import com.jbm.game.model.script.IConfigScript;

/**
 * 加载配置
 * @author JiangBangMing
 *
 * 2018年7月23日 下午2:31:19
 */

@HandlerEntity(path="/server/reloadConfig")
public class ReloadConfigHandler extends HttpHandler{

	private static final Logger logger=LoggerFactory.getLogger(ReloadConfigHandler.class);
	@Override
	public void run() {
		String auth=getString("auth");
		if(!Config.SERVER_AUTH.equals(auth)) {
			sendMsg("验证失败");
			return;
		}
		
		String tableStr=getString("table");
		String result="";
		if(tableStr!=null) {
			result=ScriptManager.getInstance().getBaseScriptEntry().functionScripts(IConfigScript.class, 
					(IConfigScript script) -> script.reloadConfig(Arrays.asList(tableStr.split(SymbolUtil.DOUHAO))));
		}else {
			result=ScriptManager.getInstance().getBaseScriptEntry().functionScripts(IConfigScript.class, 
					(IConfigScript script) -> script.reloadConfig(null));
		}
		String info=String.format("%s 加载配置:%s", MsgUtil.getIp(getSession()),result);
		logger.info(info);
		sendMsg(info);
		MailConfig mailConfig=MailManager.getInstance().getMailConfig();
		String[] recives=mailConfig.getReciveUser().toArray(new String[mailConfig.getReciveUser().size()]);
		MailManager.getInstance().sendTextMailAsync("加载配置", Config.SERVER_NAME+"\r\n"+info,recives);
	}

}
