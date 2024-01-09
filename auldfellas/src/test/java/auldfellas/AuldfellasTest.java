package auldfellas;

import static org.junit.Assert.assertEquals;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import service.auldfellas.AuldfellasMain;
import service.core.ClientInfo;
import service.core.ClientMessage;
import service.core.QuotationMessage;

public class AuldfellasTest {

	@Test
	public void testService() throws Exception {
		AuldfellasMain.main(new String[0]);
		ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://localhost:61616");
		Connection connection = factory.createConnection();
		connection.setClientID("test");
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		Queue queue = session.createQueue("QUOTATIONS");
		Topic topic = session.createTopic("APPLICATIONS");
		MessageConsumer consumer = session.createConsumer(queue);
		MessageProducer producer = session.createProducer(topic);
		connection.start();
		producer.send(session.createObjectMessage(new ClientMessage(1L,
				new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false))));
		Message message = consumer.receive();
		QuotationMessage quotationMessage = (QuotationMessage) ((ObjectMessage) message).getObject();
		System.out.println("token: " + quotationMessage.getToken());
		System.out.println("quotation: " + quotationMessage.getQuotation());
		message.acknowledge();
		assertEquals(1L, quotationMessage.getToken());
	}
}
