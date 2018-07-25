package com.jbm.game.gate.tcp.role;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.gate.script.IUserScript;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.hall.HallLoginMessage.QuitRequest;
import com.jbm.game.model.constant.Reason;

/**
 * 退出游戏
 * @author JiangBangMing
 *
 * 2018年7月25日 上午11:08:52
 */

@HandlerEntity(mid=MID.QuitReq_VALUE,msg=QuitRequest.class)
public class QuitReqHandler extends TcpHandler{

	@Override
	public void run() {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IUserScript.class, 
				script -> script.quit(getSession(), Reason.UserQuit));
	}
}
