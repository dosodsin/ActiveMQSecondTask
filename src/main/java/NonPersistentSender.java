import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class NonPersistentSender {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String queueName = "MESSAGE_QUEUE";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?create=false");
        Connection connection = connectionFactory.createConnection();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue(queueName);

        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        connection.start();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            TextMessage message = session.createTextMessage(String.valueOf(i));
            producer.send(message);
        }
        long stop = System.currentTimeMillis();
        System.out.println(stop - start);
        connection.close();
    }
}
