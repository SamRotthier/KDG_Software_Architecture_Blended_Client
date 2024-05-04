package be.kdg.sa.clients.services;

import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.repositories.AccountRepository;
import be.kdg.sa.clients.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
