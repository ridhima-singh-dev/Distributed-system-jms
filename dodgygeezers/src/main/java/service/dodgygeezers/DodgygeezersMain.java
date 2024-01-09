package service.dodgygeezers;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import service.core.ClientMessage;
import service.core.Quotation;
import service.core.QuotationMessage;

public class DodgygeezersMain {
	private static DGQService service = new DGQService();

	public static void main(String[] args) {

		String host = args.length > 0 ? args[0] : "localhost";

		ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");
	try {
		Connection connection = factory.createConnection();
		connection.setClientID("dodgygeezers");
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

		Queue queue = session.createQueue("QUOTATIONS");
		Topic topic = session.createTopic("APPLICATIONS");
		MessageConsumer consumer = session.createConsumer(topic);
		MessageProducer producer = session.createProducer(queue);
		connection.start();
		
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					ClientMessage request = (ClientMessage)((ObjectMessage) message).getObject();
					Quotation quotation = service.generateQuotation(request.getInfo());
					Message response = session.createObjectMessage(new QuotationMessage(request.getToken(), quotation));
					producer.send(response);
					message.acknowledge();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}

			
		});
	} catch (JMSException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
}