package com.jbm.game.manage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.jbm.game.engine.server.ServerInfo;
import com.jbm.game.engine.server.ServerType;
import com.jbm.game.engine.util.HttpUtil;
import com.jbm.game.manage.constant.Constant;
import com.jbm.game.manage.core.vo.ResultVO;
import com.jbm.game.manage.service.ServerService;

import io.netty.util.internal.StringUtil;

/**
 * 服务器
 * @author JiangBangMing
 *
 * 2018年7月17日 下午2:08:37
 */
@Controller
@RequestMapping("/server")
public class ServerController {

	private static final Logger logger=LoggerFactory.getLogger(ServerController.class);

	@Autowired
	private ServerService serverService;
	
	@Value("${clusteer.url}")
	private String clusterUrl;
	
	@Value("${server.auth}")
	private String serverAuth;
	
	/**
	 * 服务器列表
	 * @param session
	 * @param response
	 * @return
	 */
	public ModelAndView serverList(HttpSession session,HttpServletResponse response) {
		try {
			String url=clusterUrl+"/server/list";
			String urlGet=HttpUtil.URLGet(url);
			List<ServerInfo> servers=null;
			if(urlGet!=null) {
				servers=JSON.parseArray(urlGet,ServerInfo.class);
			}
			return new ModelAndView("/server","servers",servers);
		}catch (Exception e) {
			logger.error("服务器",e);
			return new ModelAndView("redirect:/login");
		}
	}
	
	/**
	 * 获取服务器线形图统计数据
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/statistics")
	public ResultVO<List<String>> statistics(int id){
		String url=clusterUrl+"/server/list";
		String urlGet=HttpUtil.URLGet(url);
		List<ServerInfo> servers=null;
		if(urlGet!=null) {
			servers=JSON.parseArray(urlGet,ServerInfo.class);
		}
		//计数
		Map<Integer, Integer> countMap=new HashMap<>();
		List<String> serverNameList=new ArrayList<>();//服务器名称
		List<Integer> serverOnlineList=new ArrayList<>();//服务器在线人数
		List<Integer> serverFreeMemoryList=new ArrayList<>();//服务器空闲内存
		List<Integer> serverTotalMemoryList=new ArrayList<>();//服务器可获得内存
		List<Integer> serverMaxMemoryList=new ArrayList<>();//服务器最大内存
		servers.forEach(server ->{
			if(!countMap.containsKey(server.getType())) {
				countMap.put(server.getType(), 1);
			}else {
				countMap.put(server.getType(), countMap.get(server.getType())+1);
			}
			serverNameList.add(server.getName());
			serverOnlineList.add(server.getOnline());
			serverFreeMemoryList.add(server.getFreeMemory());
			serverTotalMemoryList.add(server.getTotalMemory());
			
		});
		//样式
		List<String> serverTypeList=new ArrayList<>();
		List<Integer> serverCountList=new ArrayList<>();
		
		countMap.forEach((key,value)->{
			serverTypeList.add(ServerType.valueof(key).toString());
			serverCountList.add(value);
		});
		
		List<String> result=new ArrayList<>();
		//服务器个数统计
		result.add(JSON.toJSONString(serverTypeList));
		result.add(JSON.toJSONString(serverCountList));
		//服务器人数
		result.add(JSON.toJSONString(serverNameList));
		result.add(JSON.toJSONString(serverOnlineList));
		result.add(JSON.toJSONString(serverFreeMemoryList));
		result.add(JSON.toJSONString(serverTotalMemoryList));
		result.add(JSON.toJSONString(serverMaxMemoryList));
		
		return new ResultVO<List<String>>(Constant.CODE_SUCCESS, urlGet, result);
	} 
	
	
	/**
	 * 加载脚本
	 * @param session
	 * @param response
	 * @param ip
	 * @param port
	 * @param scriptPath
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/reloadScript")
	public ResultVO<String> reloadScript(HttpSession session,HttpServletResponse response,String ip,int port,String scriptPath){
		StringBuilder url=new StringBuilder();
		url.append("http://").append(ip).append(":").append(port).append("/server/reloadScript?auth=").append(serverAuth);
		
		if(!StringUtil.isNullOrEmpty(scriptPath)) {
			url.append("&scriptPath=").append(scriptPath);
		}
		logger.info("加载脚本:{}",url);
		String urlGet=HttpUtil.URLGet(url.toString());
		logger.info("加载返回:{}",urlGet);
		return new ResultVO<String>(Constant.CODE_SUCCESS, urlGet, urlGet);
	}
	
	/**
	 * 关闭服务器
	 * @param session
	 * @param response
	 * @param ip
	 * @param port
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/exitServer")
	public ResultVO<String> exitServer(HttpSession session,HttpServletResponse response,String ip,int port){
		StringBuilder url=new StringBuilder();
		url.append("http://").append(ip).append(":").append(port).append("/server/exit?auth=").append(serverAuth);
		logger.info("关服url:{}",url);
		String urlGet=HttpUtil.URLGet(url.toString());
		logger.info("关服返回:{}",urlGet);
		return new ResultVO<String>(Constant.CODE_SUCCESS, urlGet, urlGet);
	}
	
	/**
	 * 设置服务器状态
	 * @param session
	 * @param response
	 * @param id
	 * @param type
	 * @param state
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/state")
	public ResultVO<String> setServerState(HttpSession session,HttpServletResponse response,int id,int type,int state){
		StringBuilder url=new StringBuilder();
		url.append(clusterUrl).append("/server/state?auth=").append(serverAuth).append("&serverType").append(type).append("&serverId=")
			.append(id).append("serverState=").append(state);
		logger.info("状态URL:{}",url);
		String urlGet=HttpUtil.URLGet(url.toString());
		logger.info("状态返回:{}",urlGet);
		return new ResultVO<String>(Constant.CODE_SUCCESS, urlGet, urlGet);
	}
	/**
	 * 加载配置
	 * @param ip
	 * @param port
	 * @param tableName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/load")
	public ResultVO<String> reloadConfig(String ip,int port,String tableName){
		StringBuilder url=new StringBuilder();
		url.append("http://").append(":").append(port).append("/server/reloadConfig?auth=")
			.append(serverAuth);
		if(!StringUtil.isNullOrEmpty(tableName)) {
			url.append("&table=").append(tableName);
		}
		logger.info("加载配置:{}"+url);
		String urlGet=HttpUtil.URLGet(url.toString());
		logger.info("加载返回:{}",urlGet);
		return new ResultVO<String>(Constant.CODE_SUCCESS, urlGet, urlGet);
	}
	
	/**
	 * 获取jvm 信息
	 * @param ip
	 * @param port
	 * @return
	 */
	public ResultVO<Map<String, String>> jvmInfo(String ip,int port){
		StringBuilder url=new StringBuilder();
		url.append("http://").append(ip).append(":").append(port).append("/server/jvm/info?auth=")
			.append(serverAuth);
		logger.info("jvm 请求:{}",url);
		String urlGet=HttpUtil.URLGet(url.toString());
		logger.info("jvm 返回:{}",urlGet);
		return new ResultVO<Map<String,String>>(Constant.CODE_ERROR, urlGet, serverService.getHTMLProPerites(urlGet));
	}
}
