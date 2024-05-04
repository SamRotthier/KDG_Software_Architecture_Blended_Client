package be.kdg.sa.clients.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ProductDto {
    private UUID productId;
    private String name;
    private BigDecimal price;
    private String description;
    private int quantity;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public ProductDto(UUID productId, String name, BigDecimal price, String description, int quantity, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
