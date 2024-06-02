package be.kdg.sa.clients.controller.dto;

import java.util.UUID;

public class OrderProductDto {
    private UUID id;
    private int quantity;

    public OrderProductDto(UUID id, int quantity) {
        this.id = id;
        this.quantity = quantity;
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
}
