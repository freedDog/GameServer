package com.jbm.game.engine.script;

import java.time.LocalTime;

/**
 * 心跳脚本，最低按秒循环
 * @author JiangBangMing
 *
 * 2018年7月4日 下午2:52:59
 */
public interface ITimerEventScript extends IScript{
	
	/**
	 * 每秒执行
	 * @param localTime
	 */
	default void secondHandler(LocalTime localTime) {
		
	}
	
	default void minuteHandler(LocalTime localTime) {
		
	}
	
	default void hourHandler(LocalTime localTime) {
		
	}
	
	default void dayHandler(LocalTime localTime) {
		
	}
}
