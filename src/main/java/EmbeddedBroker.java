import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;

import java.net.URI;

public class EmbeddedBroker {
    public static void main(String[] args) throws Exception {
        BrokerService broker = new BrokerService();
        broker.setBrokerName("localhost");
        broker.setUseJmx(false);
        broker.start();
    }
}
