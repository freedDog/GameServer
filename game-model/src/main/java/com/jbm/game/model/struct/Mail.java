package com.jbm.game.model.struct;

public class Mail {

	/**
	 * 邮件状态
	 * <br>优先级依次递增
	 * @author JiangZhiYong
	 * @QQ 359135103 2017年9月21日 下午3:12:11
	 */
	public enum MailState {
		/** 未读新邮件 */
		NEW,
		/** 已读邮件 */
		READ,
		/** 领取物品 */
		GET_ITEMS,
		/** 删除 */
		DELETE;
	}

	/**
	 * 邮件类型
	 * 
	 * @author JiangZhiYong
	 * @QQ 359135103 2017年9月21日 下午3:14:51
	 */
	public enum MailType {
		/** 私人邮件 */
		PRIVATE,
		/** 公共系统邮件，全服只存储一封 */
		PUBLIC_SYSTEM,
	}
}
