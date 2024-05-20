package be.kdg.sa.clients.receivers;

import be.kdg.sa.clients.config.RabbitTopology;
import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.controller.dto.ProductDto;
import be.kdg.sa.clients.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductReceiver {
    private static final Logger logger = LoggerFactory.getLogger(ProductReceiver.class);
    private final ProductService productService;

    public ProductReceiver(ProductService productService) {
        this.productService = productService;
    }

    @RabbitListener(queues = RabbitTopology.NEW_PRODUCT_QUEUE, messageConverter = "#{jackson2JsonMessageConverter}")
    public void receiveNewProduct(ProductDto productDto) {
        logger.info("Received a new product message with name: {}", productDto.getName());
        productService.addProductFromMessage(productDto);
    }

    @RabbitListener(queues = RabbitTopology.DEACTIVATE_PRODUCT_QUEUE, messageConverter = "#{jackson2JsonMessageConverter}")
    public void receiveDeactivateProduct(OrderDto product) {
        //productService.addProduct(product.getId(), product.getName(), product.getDescription());
        System.out.println(product);
    }
}
