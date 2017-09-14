package cn.dwxmp.core.support.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import java.io.Serializable;

/**
 * 队列消息发送类
 * @author HuXinsheng
 * @version 2016年5月20日 下午3:19:19
 */
public class QueueSender {
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	/**
	 * 发送一条消息到指定的队列（目标）
	 * 
	 * @param queueName 队列名称
	 * @param message 消息内容
	 */
	public void send(String queueName, final Serializable message) {
		jmsTemplate.send(queueName, session -> session.createObjectMessage(message));
	}
}
