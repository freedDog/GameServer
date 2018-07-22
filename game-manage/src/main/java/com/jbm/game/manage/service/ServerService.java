package com.jbm.game.manage.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jbm.game.engine.util.SymbolUtil;

/**
 * 服务器
 * @author JiangBangMing
 *
 * 2018年7月17日 下午1:21:35
 */

@Service
public class ServerService {

	
	/**
	 * 获取网页属性
	 * @param string
	 * @return
	 */
	public Map<String, String> getHTMLProPerites(String string){
		Map<String, String> map=new HashMap<String, String>();
		if(string==null) {
			return map;
		}
		String[] properites=string.split("<br>");
		if(properites!=null) {
			for(String str:properites) {
				str=str.trim().replace("\n","<br>").replaceAll("\t", "&nbsp;&nbsp;");//替换换行符合缩进
				String[] keyValue=str.split(SymbolUtil.MAOHAO_REG);
				if(keyValue.length==2) {
					map.put(keyValue[0], keyValue[1]);
				}
			}
		}
		return map;
	}
}
