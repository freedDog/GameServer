package com.jbm.game.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.jbm.game.engine.util.HttpUtil;
import com.jbm.game.manage.constant.Constant;
import com.jbm.game.manage.core.vo.ResultVO;

/**
 * GM 管理
 * @author JiangBangMing
 *
 * 2018年7月17日 上午11:48:21
 */
@RequestMapping("/gm")
@Controller
public class GMController {
	
	private static final Logger logger=LoggerFactory.getLogger(GMController.class);
	
	@Value("${cluster.url}")
	private String clusterUrl;

	/**gm调用地址*/
	private String gmUrl;
	
	@Value("${server.auth}")
	private String severAuth;
	
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView gmList() {
		String url=clusterUrl+"/server/hall/ip";
		String urlGet=HttpUtil.URLGet(url);
		gmUrl="http://"+urlGet+"/gm?";
		return new ModelAndView("gm","gmUrl","gmUrl");
	}
	
	/**
	 * 
	 * @param queryString
	 * @return
	 */
	@RequestMapping("/execute")
	@ResponseBody
	public ResultVO<String> executeGm(String queryString){
		String url=gmUrl+"auth="+severAuth+queryString;
		String urlGet=HttpUtil.URLGet(url);
		logger.info("gm:{}"+url);
		return new ResultVO<String>(Constant.CODE_SUCCESS,urlGet, urlGet);
	}
	
}
