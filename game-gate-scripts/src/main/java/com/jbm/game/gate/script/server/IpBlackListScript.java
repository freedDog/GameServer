package com.jbm.game.gate.script.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.filter.firewall.BlacklistFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.script.IInitScript;
import com.jbm.game.gate.script.IGateServerScript;
import com.jbm.game.gate.server.GateTcpUserServer;

/**
 * 设置Ip黑名单
 * TODO 是否需要存入数据库，通过后台调用设置
 * @author JiangBangMing
 *
 * 2018年7月24日 下午5:59:49
 */
public class IpBlackListScript implements IGateServerScript,IInitScript{
	
	private static final Logger logger=LoggerFactory.getLogger(IpBlackListScript.class);
	private List<InetAddress> blackList;
	@Override
	public void init() {
		blackList=new ArrayList<>();
		try {
//			blackList.add(InetAddress.getByName("192.168.0.17"));//TODO 测试
		}catch (Exception e) {
			logger.warn("添加IP 黑名单",e);
		}
		
		//设置用户TCP 黑名单
		GateTcpUserServer.getBlacklistFilter().setBlacklist(blackList);
	}
	
	@Override
	public void setIpBlackList(BlacklistFilter filter) {
		filter.setBlacklist(blackList);
	}
}
