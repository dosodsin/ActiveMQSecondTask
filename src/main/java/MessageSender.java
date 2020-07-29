import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MessageSender {

    private String queueName;
    private String url;
    private int mode;
    private boolean isTransacted;

    public MessageSender(String queueName, String url, int mode, boolean isTransacted) {
        this.queueName = queueName;
        this.url = url;
        this.mode = mode;
        this.isTransacted = isTransacted;
    }

    public void sendMessage(String url, int mode, boolean isTransacted) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(isTransacted, mode);
            Destination destination = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(destination);
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                TextMessage message = session.createTextMessage(String.valueOf(i));
                producer.send(message);
            }
            long stop = System.currentTimeMillis();
            System.out.println(stop - start);

            session.close();
            connection.close();
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }

}
