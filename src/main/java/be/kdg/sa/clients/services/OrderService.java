package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.repositories.AccountRepository;
import be.kdg.sa.clients.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository=orderRepository;
    }


    public Optional<Order> getOrderByOrderId(UUID givenOrderId) {
        return orderRepository.findOrderByOrderId(givenOrderId);
    }

    public Order createOrder(OrderDto orderDto){
        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setProducts(orderDto.getProducts());
        order.setStatus(orderDto.getStatus());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setCreationDateTime(orderDto.getCreationDateTime());
        order.setModifiedDateTime(orderDto.getCreationDateTime());
        order.setCreationDateTime(orderDto.getCreationDateTime()); //java.time.LocalDateTime.now()
        order.setAccount(orderDto.getAccount());

        return orderRepository.save(order);
    }

    public void confirmOrder(Optional<Order> foundOrder) {
        foundOrder.ifPresent(order -> order.setStatus(OrderStatus.CONFIRMED));
    }

    public void cancelOrder(Optional<Order> foundOrder) {
        foundOrder.ifPresent(order -> order.setStatus(OrderStatus.CANCELLED));
    }

    public Optional<List<Order>> getOrders(){
        return Optional.of(orderRepository.findAll());
    }

}
