package be.kdg.sa.clients.receivers;

import be.kdg.sa.clients.config.RabbitTopology;
import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.controller.dto.ProductDto;
import be.kdg.sa.clients.services.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductReceiver {

    private final ProductService productService;

    public ProductReceiver(ProductService productService) {
        this.productService = productService;
    }

    @RabbitListener(queues = RabbitTopology.NEW_PRODUCT_QUEUE, messageConverter = "#{jackson2JsonMessageConverter}")
    public void receiveProduct(OrderDto product) {
        //productService.addProduct(product.getId(), product.getName(), product.getDescription());
        System.out.println(product);
    }
}
