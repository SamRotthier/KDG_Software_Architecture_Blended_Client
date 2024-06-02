package be.kdg.sa.clients.sender;

import be.kdg.sa.clients.controller.dto.OrderProductDto;
import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.OrderProduct;
import be.kdg.sa.clients.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderProductMessage {
    private UUID orderId;

    private List<OrderProductDto> products;

    private UUID accountId;

    public OrderProductMessage(UUID orderId, List<OrderProductDto> products, UUID accountId) {
        this.orderId = orderId;
        this.products = products;
        this.accountId = accountId;
    }

    public OrderProductMessage() {

    }

    public UUID getOrderId() {
        return orderId;
    }

    public List<OrderProductDto> getProducts() {
        return products;
    }

    public UUID getAccountId() {
        return accountId;
    }
}
