package com.jbm.game.hall.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件管理
 * <p>
 * 个人邮件单独存储，系统通用邮件只存一封,直接操作mongodb，不缓存
 * </p>
 * @author JiangBangMing
 *
 * 2018年7月25日 下午3:41:59
 */
public class MailManager {

	private static final Logger logger=LoggerFactory.getLogger(MailManager.class);
	private static volatile MailManager mailManager;
	
	private MailManager() {
		
	}
	
	public static MailManager getInstance() {
		if(mailManager==null) {
			synchronized (MailManager.class) {
				if(mailManager==null) {
					mailManager=new MailManager();
				}
			}
		}
		return mailManager;
	}
	

}
