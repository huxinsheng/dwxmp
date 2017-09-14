package cn.dwxmp.core.util;

import cn.dwxmp.core.support.email.Email;
import cn.dwxmp.core.support.email.EmailSender;

/**
 * 发送邮件辅助类
 * 
 * @author HuXinsheng
 * @version $Id: MailUtil.java, v 0.1 2014年12月4日 下午8:22:43 HuXinsheng Exp $
 */
public final class EmailUtil {
	private EmailUtil() {
	}

	/**
	 * 发送邮件
	 */
	public static final boolean sendEmail(Email email) {
		// 初始化邮件引擎
		EmailSender sender = new EmailSender(email.getHost());
		sender.setNamePass(email.getName(), email.getPassword(), email.getKey());
		if (!sender.setFrom(email.getFrom()))
			return false;
		if (!sender.setTo(email.getSendTo()))
			return false;
		if (email.getCopyTo() != null && !sender.setCopyTo(email.getCopyTo()))
			return false;
		if (!sender.setSubject(email.getTopic()))
			return false;
		if (!sender.setBody(email.getBody()))
			return false;
		if (email.getFileAffix() != null) {
			for (int i = 0; i < email.getFileAffix().length; i++) {
				if (!sender.addFileAffix(email.getFileAffix()[i]))
					return false;
			}
		}
		// 发送
		return sender.sendout();
	}
}
