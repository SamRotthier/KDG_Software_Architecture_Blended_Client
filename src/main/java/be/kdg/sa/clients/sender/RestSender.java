package be.kdg.sa.clients.sender;

import be.kdg.sa.clients.config.RabbitTopology;
import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.controller.dto.OrderProductDto;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.domain.OrderProduct;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class RestSender {

    private static final Logger logger = LoggerFactory.getLogger(RestSender.class);
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;


    public RestSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/order/{uuid}")
    public void sendOrder(@RequestBody Order order) {
        logger.info("Send order message for UUID: {}", order.getOrderId());

        //debug
        List<OrderProduct> productsList = order.getProducts();
        for (OrderProduct product : productsList) {
            System.out.println("ID: " + product.getId() + ", ProductId: " + product.getProduct().getProductId() + ", OrderId: "+ product.getOrder().getOrderId() + ", Quantity: " + product.getQuantity());
        }

        //message
        OrderProductMessage message = new OrderProductMessage(order.getOrderId(), order.getProducts().stream().map(i -> new OrderProductDto(i.getId(), i.getOrder().getOrderId(), i.getProduct().getProductId(),i.getQuantity())).toList(), order.getAccount().getAccountId());

        rabbitTemplate.convertAndSend(RabbitTopology.TOPIC_EXCHANGE, "order-product-queue",
                message);

        //debug
        for (OrderProductDto product : message.getProducts()) {
            System.out.println("ID: " + product.getId() + ", ProductId: " + product.getProductId() + ", OrderId: "+ product.getOrderId() + ", Quantity: " + product.getQuantity());
        }

        logger.info("Order message was successfully posted to the ORDER_PRODUCT_QUEUE for UUID: {}",order.getOrderId());
    }

}
