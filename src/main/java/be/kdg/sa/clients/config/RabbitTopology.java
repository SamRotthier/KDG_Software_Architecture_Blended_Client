package be.kdg.sa.clients.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Works with rabbit amqp
@Configuration
public class RabbitTopology {
    public final static String NEW_PRODUCT_CLIENT_QUEUE = "new-product-client-queue";
    public final static String ORDER_PRODUCT_QUEUE = "order-product-queue";
    public final static String PRODUCT_STATE_QUEUE = "product-state-queue";
    public final static String CONFIRM_ORDER_QUEUE = "confirm-order-queue";
    public static final String TOPIC_EXCHANGE = "bakery-exchange";


    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Queue newProductQueue() {
        return new Queue(NEW_PRODUCT_CLIENT_QUEUE, false);
    }


    @Bean
    public Binding topicNewProductBinding() {
        return BindingBuilder.bind(newProductQueue()).to(topicExchange()).with(NEW_PRODUCT_CLIENT_QUEUE);
    }

    @Bean
    public Queue newOrderProductQueue() {
        return new Queue(ORDER_PRODUCT_QUEUE, false);
    }


    @Bean
    public Binding topicOrderProductBinding() {
        return BindingBuilder.bind(newOrderProductQueue()).to(topicExchange()).with(ORDER_PRODUCT_QUEUE);
    }

    @Bean
    public Queue newProductStateQueue() {
        return new Queue(PRODUCT_STATE_QUEUE, false);
    }


    @Bean
    public Binding topicProductStateBinding() {
        return BindingBuilder.bind(newProductStateQueue()).to(topicExchange()).with(PRODUCT_STATE_QUEUE);
    }

    @Bean
    public Queue newConfirmOrderQueue() {
        return new Queue(CONFIRM_ORDER_QUEUE, false);
    }


    @Bean
    public Binding topicConfirmOrderBinding() {
        return BindingBuilder.bind(newConfirmOrderQueue()).to(topicExchange()).with(CONFIRM_ORDER_QUEUE);
    }

    @Bean
    RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}