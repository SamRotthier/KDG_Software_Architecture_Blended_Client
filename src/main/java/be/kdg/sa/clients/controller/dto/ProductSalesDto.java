package be.kdg.sa.clients.controller.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductSalesDto {
    private UUID productId;
    private String name;
    private BigDecimal price;

    private int quantity;
    private BigDecimal total;

    public ProductSalesDto(UUID productId, String name, BigDecimal price, int quantity, BigDecimal total) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    public ProductSalesDto() {
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
