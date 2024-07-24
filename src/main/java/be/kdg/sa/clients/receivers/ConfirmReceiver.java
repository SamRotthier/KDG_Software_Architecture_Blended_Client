package be.kdg.sa.clients.receivers;

import be.kdg.sa.clients.config.RabbitTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConfirmReceiver {
    private static final Logger logger = LoggerFactory.getLogger(ConfirmReceiver.class);

    public ConfirmReceiver() {
    }

    @RabbitListener(queues = RabbitTopology.CONFIRM_ORDER_QUEUE)
    public static void receiveConfirmOrder(String message){
        logger.info("Received a confirmation: {}", message);
    }
}
