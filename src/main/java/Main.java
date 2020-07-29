import javax.jms.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main {
    private static final Logger logger = LogManager.getLogger();

    private static final String URL = "vm://broker";
    private static final String QUEUENAME = "MESSAGE_QUEUE";
    private static final boolean ISPERSISTENT = false;
    private static final boolean ISTRANSACTED = false;

    private static final int MODE_ACKNOWLEDGE = Session.AUTO_ACKNOWLEDGE;
    private static final int MODE_TRANSACTED = Session.SESSION_TRANSACTED;
    private static final int MODE_DUPS_OK_ACKNOWLEDGE = Session.DUPS_OK_ACKNOWLEDGE;
    private static final int MODE_CLIENT_ACKNOWLEDGE = Session.CLIENT_ACKNOWLEDGE;

    public static void main(String[] args) {
        MessageSender messageSender = new MessageSender(QUEUENAME, URL, MODE_ACKNOWLEDGE, ISTRANSACTED);
        MessageReceiver messageReceiver = new MessageReceiver(QUEUENAME, URL, MODE_ACKNOWLEDGE, ISTRANSACTED);

        try {
            EmbeddedBroker broker = new EmbeddedBroker(URL, QUEUENAME, ISPERSISTENT, ISTRANSACTED);
            broker.runBroker();

            messageSender.sendMessage(URL, MODE_ACKNOWLEDGE, ISTRANSACTED);
            messageReceiver.receiveMessage(URL, QUEUENAME, MODE_ACKNOWLEDGE, ISTRANSACTED);


        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }
}
