package be.kdg.sa.clients.sender;

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

    private List<OrderProduct> products;
    private BigDecimal totalPrice;



    public OrderProductMessage(UUID orderId, List<OrderProduct> products, BigDecimal totalPrice) {
        this.orderId = orderId;
        this.products = products;
        this.totalPrice = totalPrice;
    }
    public OrderProductMessage() {

    }

    public UUID getOrderId() {
        return orderId;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

}
