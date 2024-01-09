package broker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import broker.sender.BrokerMain;
import service.core.ClientInfo;
import service.core.ClientMessage;
import service.core.OfferMessage;
import service.core.Quotation;

public class BrokerTest {
	private Connection connection;
	private Session session;
	private ClientInfo clientInfo;

	@Before
	public void setUp() throws JMSException {
		ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://localhost:61616");
		connection = factory.createConnection();
		connection.setClientID("test-client");
		session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		connection.start();

		clientInfo = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false);
	}

	
	@Test
    public void testService() throws Exception {
        BrokerMain.main(new String[0]);
        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://localhost:61616");
        Connection connection = factory.createConnection();
        connection.setClientID("test");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("OFFERS");
        Topic topic = session.createTopic("APPLICATIONS");
        MessageConsumer consumer = session.createConsumer(queue);
        MessageProducer producer = session.createProducer(topic);
        connection.start();

        producer.send(
                session.createObjectMessage(
                        new ClientMessage(1L, new ClientInfo("Niki Collier",
                                ClientInfo.FEMALE, 49, 1.5494, 80, false,
                                false))));
        Message message = consumer.receive();
        OfferMessage offerMessage = (OfferMessage) ((ObjectMessage) message).getObject();
        System.out.println("client info: " + offerMessage.getInfo());
        message.acknowledge();
        assertEquals(0, offerMessage.getQuotations().size());
    }
	

}
