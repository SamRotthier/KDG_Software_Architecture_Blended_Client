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
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public ProductDto(UUID productId, String name, BigDecimal price, LocalDateTime createdDate) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.createdDate = createdDate;
    }

    public ProductDto(UUID productId, String name, LocalDateTime createdDate) {
        this.productId = productId;
        this.name = name;
        this.createdDate = createdDate;
    }

    public ProductDto() {
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

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
