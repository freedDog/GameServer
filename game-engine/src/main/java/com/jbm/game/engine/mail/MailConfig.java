package com.jbm.game.engine.mail;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 邮件发配置
 * @author JiangBangMing
 *
 * 2018年7月17日 上午10:49:17
 */

@Root
public class MailConfig {

	//协议地址
	@Element(required=false)
	private String mailSmtpHost="smtp.exmail.qq.com";
	
	//ssl
	@Element(required=false)
	private String mailSmtpSslEnable="false";
	
	//验证
	@Element(required=false)
	private String mailSmtpAuth="true";
}
