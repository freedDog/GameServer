package com.jbm.game.gate.struct;

/**
 * 全局原因，类别区别
 * @author JiangBangMing
 *
 * 2018年7月21日 下午2:34:58
 */
public enum Reason {

	SessionIdle("会话空闲"),
	SessionClosed("会话关闭"),
	
	ServerClose("服务器关闭"),
	;
	private String reason;
	
	private Reason(String reason) {
		this.reason=reason;
	}
	
	public String getReason() {
		return this.reason;
	}
}
