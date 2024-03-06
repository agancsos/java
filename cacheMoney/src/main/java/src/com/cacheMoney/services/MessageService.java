package com.cacheMoney.services;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueBrowser;
import javax.jms.Queue;
import java.util.Enumeration;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.ArrayList;

public class MessageService {
	private static MessageService instance = null;
	private ActiveMQConnectionFactory factory = null;
	private ConfigurationService configService = ConfigurationService.getInstance("");

	private MessageService() {
		if (this.configService.getProperty("mq.url", null) != null) {
			if (this.configService.getProperty("mq.username", null) != null && this.configService.getProperty("mq.password", null) != null) {
				this.factory = new ActiveMQConnectionFactory((String)this.configService.getProperty("mq.username", ""),
				SecurityService.getBase64Decoded((String)this.configService.getProperty("mq.password", "")),
				(String)this.configService.getProperty("mq.url", ""));
			} else {
				this.factory = new ActiveMQConnectionFactory((String)this.configService.getProperty("mq.url", ""));
			}
		}
	}

	public static MessageService getInstance() {
		if (MessageService.instance == null) {
			MessageService.instance = new MessageService();
		}
		return MessageService.instance;
	}

	public void putMessagege(String queueName, String message) throws Exception {
		if (this.isReachable(false)) {
			Connection conn = this.factory.createConnection();
			conn.start();
			ActiveMQSession session = (ActiveMQSession)conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = session.createQueue(queueName);
			MessageProducer prod = session.createProducer(dest);
			prod.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			Message msg = session.createTextMessage(message);
			prod.send(msg);
			session.close();
			conn.close();
		}
	}

	public Message[] readMessages(String queueName) throws Exception {
		ArrayList<Message> rst = new ArrayList<Message>();
		if (this.isReachable(false)) {
            Connection conn = this.factory.createConnection();
            conn.start();
            ActiveMQSession session = (ActiveMQSession)conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination dest = session.createQueue(queueName);
            MessageConsumer cons = session.createConsumer(dest);
			System.out.println(">> Scanning messages");
			QueueBrowser browser = session.createBrowser((Queue)dest);
			Enumeration enumerator = browser.getEnumeration();
			int messageCount = 0;
			while (enumerator.hasMoreElements() && messageCount < 10000) {
				Message msg = (Message)enumerator.nextElement();
				cons.receive();
				rst.add(msg);
				messageCount++;
			}
			System.out.println(">>> Back");
			cons.close();
            session.close();
            conn.close();
        }
		return rst.toArray(new Message[rst.size()]);
	}

	public boolean isReachable(boolean override) {
		try {
			Connection conn = this.factory.createConnection();
			conn.start();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			conn.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			if (override) {
				return true;
			} else {
				return false;
			}
		}
	}
}

