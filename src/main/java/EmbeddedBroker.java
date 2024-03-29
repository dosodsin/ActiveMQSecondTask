import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.activemq.broker.BrokerService;

public class EmbeddedBroker {
    private static final Logger logger = LogManager.getLogger();

    private String url;
    private String queueName;
    private boolean isPersistent;
    private boolean isTransacted;

    public EmbeddedBroker(String url, String queueName, boolean isPersistent, boolean isTransacted) {
        this.url = url;
        this.queueName = queueName;
        this.isPersistent = isPersistent;
        this.isTransacted = isTransacted;
    }

    public void runBroker() {
        BrokerService brokerService = new BrokerService();

        try {
            brokerService.addConnector(url);
            brokerService.setBrokerName("broker");
            brokerService.setPersistent(isPersistent);
            brokerService.setUseJmx(false);
            brokerService.start();
        } catch (Exception ex) {
            logger.error("port already occupied",ex.getMessage());
        }
    }

}
