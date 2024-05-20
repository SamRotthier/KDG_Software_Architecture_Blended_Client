package be.kdg.sa.clients.sender;

import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderProductMessage {
    private UUID orderId;

    private List<Product> products;
    private double totalPrice;



    public OrderProductMessage(UUID orderId, List<Product> products, double totalPrice) {
        this.orderId = orderId;
        this.products = products;
        this.totalPrice = totalPrice;
    }
    public OrderProductMessage() {

    }

    public UUID getOrderId() {
        return orderId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

}
