import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.*;
import java.io.*;
import java.util.Properties;

public class MessageReceiver {
    private static final Logger logger = LogManager.getLogger();

    private String queueName;
    private String url;
    private int mode;
    private boolean isTransacted;

    public MessageReceiver(String queueName, String url, int mode, boolean isTransacted) {
        this.queueName = queueName;
        this.url = url;
        this.mode = mode;
        this.isTransacted = isTransacted;
    }

    public void receiveMessage(String url, String queueName, int mode, boolean isTransacted, String pathToFinalFile) {
        File file = new File(pathToFinalFile);
        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            logger.error("file not found",e.getMessage());
        }

        Connection connection = null;
        Session session = null;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

        try {
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(isTransacted, mode);
            Destination destination = session.createQueue(queueName);

            MessageConsumer consumer = session.createConsumer(destination);
            long start = System.currentTimeMillis();
            printWriter.write("Start" + "\n");
            for (int i = 0; i < 1000; i++) {
                Message message = consumer.receive();
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    printWriter.write(textMessage.getText() + "\n");
                }
            }
            long stop = System.currentTimeMillis();

            printWriter.write("Time: " + (stop - start));
            printWriter.close();
            session.close();
            connection.close();
        } catch (JMSException ex) {
            logger.error("problem with connection",ex.getMessage());
        }
    }

}
