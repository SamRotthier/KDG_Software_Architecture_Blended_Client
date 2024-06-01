package be.kdg.sa.clients.sender;

import be.kdg.sa.clients.config.RabbitTopology;
import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class RestSender {

    private static final Logger logger = LoggerFactory.getLogger(RestSender.class);
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    private final OrderService orderService;

    public RestSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper,OrderService orderService) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.orderService= orderService;
    }

    @PostMapping("/deliver/{uuid}")
    public void sendOrder(@RequestBody Optional<Order> order) {
        logger.info("Trying to send order message for UUID: {}", order.get().getOrderId());

        rabbitTemplate.convertAndSend(RabbitTopology.ORDER_PRODUCT_QUEUE, "order-product-queue",
                (new OrderProductMessage(order.get().getOrderId(), order.get().getProducts(), order.get().getTotalPrice())));
        logger.info("Delivery message was successfully posted to the ORDER_PRODUCT_QUEUE for UUID: {}",order.get().getOrderId());
    }

}
