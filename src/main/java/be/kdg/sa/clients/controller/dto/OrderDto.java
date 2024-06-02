package be.kdg.sa.clients.controller.dto;

import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.OrderProduct;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderDto {

    private UUID orderId;

    private List<OrderProductDto> products;
    private OrderStatus status;
    private double totalPrice;
    private LocalDateTime creationDateTime;

    private UUID accountid;


    public OrderDto(List<OrderProductDto> products, OrderStatus status, double totalPrice, UUID accountid) {
        this.products = products;
        this.status = status;
        this.totalPrice = totalPrice;
        this.accountid = accountid;
    }

    public OrderDto(List<OrderProductDto> products, UUID accountid) {
        this.products = products;
        this.accountid = accountid;
    }

    public OrderDto() {

    }

    public UUID getOrderId() {
        return orderId;
    }

    public List<OrderProductDto> getProducts() {
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

    public UUID getAccountId() {
        return accountid;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setProducts(List<OrderProductDto> products) {
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

    public void setAccountId(UUID accountid) {
        this.accountid = accountid;
    }

}





