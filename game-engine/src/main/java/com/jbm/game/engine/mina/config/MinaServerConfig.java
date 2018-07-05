package com.jbm.game.engine.mina.config;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.jbm.game.engine.server.BaseServerConfig;
import com.jbm.game.engine.server.ServerType;

/**
 * 主要配置Mina服务器相关配置
 * @author JiangBangMing
 *
 * 2018年7月5日 上午11:46:40
 */
@Root
public class MinaServerConfig extends BaseServerConfig{

	//mina地址
	@Element(required=false)
	private String ip;
	
	//mina端口
	@Element(required=false)
	private int port=8500;
	
	//http 服务器地址
	@Element(required=false)
	private String url;
	
	//tcp没有延迟
	@Element(required=false)
	private boolean tcpNoDelay=true;
	
	//是否重用地址
	@Element(required=false)
	private boolean reuseAddress=true;
	
	//读取空闲时间检测
	@Element(required=false)
	private int readerIdleTime=120;
	
	//写入空闲时间检测
	@Element(required=false)
	private int writeIdleTime=120;
	
	@Element(required=false)
	private int soLinger=0;
	
	//服务器类型
	@Element(required=false)
	private ServerType type=ServerType.GATE;
	
	//http服务器端口
	@Element(required=false)
	private int httpPort;
	
	//网络带宽:负载均衡时做判断依据，已1M支撑64人并发计算
	@Element(required=false)
	private int netSpeed=64*5;
	
	//限制每秒会话接受的消息数，超过则关闭
	@Element(required=false)
	private int maxCountPerSecond=30;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public boolean isReuseAddress() {
		return reuseAddress;
	}

	public void setReuseAddress(boolean reuseAddress) {
		this.reuseAddress = reuseAddress;
	}

	public int getReaderIdleTime() {
		return readerIdleTime;
	}

	public void setReaderIdleTime(int readerIdleTime) {
		this.readerIdleTime = readerIdleTime;
	}

	public int getWriteIdleTime() {
		return writeIdleTime;
	}

	public void setWriteIdleTime(int writeIdleTime) {
		this.writeIdleTime = writeIdleTime;
	}

	public int getSoLinger() {
		return soLinger;
	}

	public void setSoLinger(int soLinger) {
		this.soLinger = soLinger;
	}

	public ServerType getType() {
		return type;
	}

	public void setType(ServerType type) {
		this.type = type;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public int getNetSpeed() {
		return netSpeed;
	}

	public void setNetSpeed(int netSpeed) {
		this.netSpeed = netSpeed;
	}

	public int getMaxCountPerSecond() {
		return maxCountPerSecond;
	}

	public void setMaxCountPerSecond(int maxCountPerSecond) {
		this.maxCountPerSecond = maxCountPerSecond;
	}
	
	
}
