package be.kdg.sa.clients.controller.dto;

import java.util.UUID;

public class OrderProductDto {
    private UUID id;
    private int quantity;
    private UUID orderId;
    private UUID productId;

    public OrderProductDto(UUID id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public OrderProductDto(UUID id, UUID orderId, UUID productId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderProductDto(UUID orderId, UUID productId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderProductDto() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
}
