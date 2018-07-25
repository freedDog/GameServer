package com.jbm.game.hall.http.gm;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.HttpHandler;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.util.JsonUtil;
import com.jbm.game.engine.util.MsgUtil;
import com.jbm.game.hall.server.HallServer;
import com.jbm.game.model.constant.Config;

/**
 * GM 处理器
 * @author JiangBangMing
 *
 * 2018年7月25日 下午2:51:46
 */
@HandlerEntity(path="/gm")
public class GmHandler extends HttpHandler{

		private static final Logger logger=LoggerFactory.getLogger(GmHandler.class);
		private static final Map<String, Method> GM_METHOD=JsonUtil.getFieldMethodMap(GmHandler.class, null);
		
		@Override
		public void run() {
			String auth=getString("auth");
			MinaServerConfig minaServerConfig=HallServer.getInstance().getHallHttpServer().getMinaServerConfig();
			if(!Config.SERVER_AUTH.equals(auth)||!minaServerConfig.getIp().startsWith("192")||!minaServerConfig.getIp().startsWith("127")) {
				sendMsg("权限验证失败");
				return;
			}
			
			String result=execute();
			logger.info("{} 使用GM结果:{}",MsgUtil.getIp(getSession()),result);
			if(getSession()!=null) {
				sendMsg(result);
			}
		}
		
		/**
		 * 执行命令
		 * @return
		 */
		public String execute() {
			String cmd=getString("cmd");//命令，方法名称
			if(cmd.equalsIgnoreCase("execute")) {
				return "指令错误";
			}
			String result=String.format("GM %s 执行失败", getMessage().getQueryString());
			if(GM_METHOD.containsKey(cmd.trim())) {
				Method method=GM_METHOD.get(cmd);
				try {
					result=(String)method.invoke(this);
				}catch (Exception e) {
					logger.error("GM",e);
				}
			}
			return result;
		}
	
}
