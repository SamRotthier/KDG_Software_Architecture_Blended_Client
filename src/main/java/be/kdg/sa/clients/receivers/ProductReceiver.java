package be.kdg.sa.clients.receivers;

import be.kdg.sa.clients.config.RabbitTopology;
import be.kdg.sa.clients.controller.dto.ProductDto;
import be.kdg.sa.clients.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductReceiver {
    private static final Logger logger = LoggerFactory.getLogger(ProductReceiver.class);
    private final ProductService productService;

    public ProductReceiver(ProductService productService) {
        this.productService = productService;
    }

    @RabbitListener(queues = RabbitTopology.NEW_PRODUCT_CLIENT_QUEUE, messageConverter = "#{jackson2JsonMessageConverter}")
    public void receiveNewProduct(ProductDto productDto) {
        logger.info("Received a new product message with name: {}", productDto.getName());
        productService.addProductFromMessage(productDto);
    }

    @RabbitListener(queues = RabbitTopology.PRODUCT_STATE_QUEUE, messageConverter = "#{jackson2JsonMessageConverter}")
    public void receiveDeactivateProduct(UUID productNumber) {
        logger.info("Received a new deactivation message for product id: {}", productNumber);
        productService.changeProductStateById(productNumber);

    }
}
