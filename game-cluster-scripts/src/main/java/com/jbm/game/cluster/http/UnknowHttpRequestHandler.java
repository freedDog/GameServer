package com.jbm.game.cluster.http;

import org.apache.mina.http.api.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.HttpHandler;

/**
 * 未知请求 404
 * @author JiangBangMing
 *
 * 2018年7月25日 下午1:15:54
 */
@HandlerEntity(path="")
public class UnknowHttpRequestHandler extends HttpHandler{
	
	private static final Logger logger=LoggerFactory.getLogger(UnknowHttpRequestHandler.class);
	
	@Override
	public void run() {
		logger.info("{} 请求页面错误",getSession().getRemoteAddress().toString());
		getParameter().setStatus(HttpStatus.CLIENT_ERROR_NOT_FOUND);
		responseWithStatus();
	}
}
