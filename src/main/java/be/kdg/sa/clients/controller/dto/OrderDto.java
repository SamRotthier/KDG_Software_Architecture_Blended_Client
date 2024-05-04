package be.kdg.sa.clients.controller.dto;

import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.domain.Product;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/*
public record OrderDto(UUID orderId){
    public OrderDto {
    }
}
*/


public record OrderDto(UUID orderId, UUID accountId , LocalDateTime creationDateTime , OrderStatus status, double totalPrice, List<Product> products) {
    public OrderDto(Order order) {
        this(order.getOrderId(),
                order.getAccount().getAccountId(),
                order.getCreationDateTime(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getProducts()
                );
    }
}

// order.getProducts().stream().map(ProductDto::new).toList());




