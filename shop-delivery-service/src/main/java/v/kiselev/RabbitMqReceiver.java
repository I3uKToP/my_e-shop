package v.kiselev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import v.kiselev.dto.OrderMessage;

public class RabbitMqReceiver {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);

    private final AmqpTemplate rabbitTemplate;


    public RabbitMqReceiver(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitHandler
    public void receive(OrderMessage orderMessage) {
        logger.info("New order with id: " + orderMessage.getId());

        new Thread(()->{
            for(OrderStatus status : OrderStatus.values()) {
                try {
                    Thread.sleep(15000);
                    orderMessage.setState(status.name());
                    logger.info("Changing status for order '{}' to '{}'", orderMessage.getId(), status.name());
                    rabbitTemplate.convertAndSend("order.exchange", "processed_order", orderMessage);
                } catch (InterruptedException ex) {
                    logger.error(String.valueOf(ex));
                    break;
                }
            }
        }).start();
    }

    public enum OrderStatus {
        CREATED, PROCESSED, IN_DELIVERY, DELIVERED, CLOSED, CANCELED
    }
}
