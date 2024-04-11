package be.kdg.sa.clients.domain;

import be.kdg.sa.clients.domain.Enum.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {
    private UUID orderId;
    private List<Product> products;
    private OrderStatus status;
    //private boolean cancelRequest;
    private double totalPrice;
    private LocalDateTime creationDateTime;
    private LocalDateTime modifiedDateTime;
    private LocalDateTime confirmationDateTime;
    private Account account;


    public Order(UUID orderId, List<Product> products, OrderStatus status, double totalPrice, LocalDateTime creationDateTime, LocalDateTime modifiedDateTime, LocalDateTime confirmationDateTime, Account account) {
        this.orderId = orderId;
        this.products = products;
        this.status = status;
        this.totalPrice = totalPrice;
        this.creationDateTime = creationDateTime;
        this.modifiedDateTime = modifiedDateTime;
        this.confirmationDateTime = confirmationDateTime;
        this.account = account;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public LocalDateTime getConfirmationDateTime() {
        return confirmationDateTime;
    }

    public Account getAccount() {
        return account;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public void setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public void setConfirmationDateTime(LocalDateTime confirmationDateTime) {
        this.confirmationDateTime = confirmationDateTime;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void cancelOrder (){
    if (status == OrderStatus.PENDING){
        status = OrderStatus.CANCELLED;
    }
 }

}
