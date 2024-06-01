package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.repositories.AccountRepository;
import be.kdg.sa.clients.repositories.OrderRepository;
import be.kdg.sa.clients.sender.RestSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private RestSender restSender;


    @Autowired
    public OrderService(OrderRepository orderRepository, RestSender restSender) {
        this.orderRepository=orderRepository;
        this.restSender = restSender;
    }


    public Optional<Order> getOrderByOrderId(UUID givenOrderId) {
        return orderRepository.findOrderByOrderId(givenOrderId);
    }

    public Order createOrder(OrderDto orderDto){
        logger.info("Creating new order");
        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setProducts(orderDto.getProducts());
        order.setStatus(orderDto.getStatus());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setCreationDateTime(orderDto.getCreationDateTime());
        order.setAccount(orderDto.getAccount());

        return orderRepository.save(order);
    }

    public void confirmOrder(Optional<Order> foundOrder) {
        foundOrder.ifPresent(order -> {
            logger.info("Confirming order with ID: {}", order.getOrderId());
            order.setStatus(OrderStatus.CONFIRMED);
            restSender.sendOrder(foundOrder);
        });
    }

    public void cancelOrder(Optional<Order> foundOrder) {
        foundOrder.ifPresent(order -> {
            logger.info("Cancelling order with ID: {}", order.getOrderId());
            order.setStatus(OrderStatus.CANCELLED);
        });
    }

    public Optional<List<Order>> getOrders(){
        logger.info("Fetching all orders");
        return Optional.of(orderRepository.findAll());
    }

}
