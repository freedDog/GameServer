package com.jbm.game.hall.script;

import java.util.function.Consumer;

import com.jbm.game.engine.script.IScript;
import com.jbm.game.model.struct.Mail;
import com.jbm.game.model.struct.Mail.MailType;

/**
 * 邮件
 * @author JiangBangMing
 *
 * 2018年7月25日 下午3:27:29
 */
public interface IMailScript extends IScript{

	
	/**
	 * 发送邮件
	 * @param senderId
	 * @param receiverId
	 * @param title
	 * @param content
	 * @param type
	 * @param mailConsumer
	 */
	public default void sendMail(long senderId,long receiverId,String title,String content,MailType type,Consumer<Mail> mailConsumer) {
		
	}
}
