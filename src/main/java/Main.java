import javax.jms.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Main {
    private static final Logger logger = LogManager.getLogger();

    private static String url = "";
    private static String queueName = "";
    private static boolean isPersistent = false;
    private static boolean isTransacted = false;
    private static int sessionMode = 0;

    public static void main(String[] args) {
        Properties properties = new Properties();

        try (InputStream inputStream = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(inputStream);
            url = properties.getProperty("broker.url");
            queueName = properties.getProperty("queueName");
            isPersistent = (Boolean.parseBoolean(properties.getProperty("isPersistent")));
            isTransacted = (Boolean.parseBoolean(properties.getProperty("isTransacted")));
            sessionMode = (Integer.parseInt(properties.getProperty("sessionMode")));

        } catch (IOException ex) {
            logger.error("file with properties not found",ex.getMessage());
        }

        MessageSender messageSender = new MessageSender(queueName, url, sessionMode, isTransacted);
        MessageReceiver messageReceiver = new MessageReceiver(queueName, url, sessionMode, isTransacted);

        try {
            EmbeddedBroker broker = new EmbeddedBroker(url, queueName, isPersistent, isTransacted);
            broker.runBroker();

            messageSender.sendMessage(url, sessionMode, isTransacted);
            messageReceiver.receiveMessage(url, queueName, sessionMode, isTransacted);


        } catch (Exception ex) {
            logger.error("Exception", ex.getMessage());
        }
    }
}