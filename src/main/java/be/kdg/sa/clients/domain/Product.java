package be.kdg.sa.clients.domain;

import java.time.LocalDateTime;

// @Entity
public class Product {
    private String name;
    private Double price;
    private String description;
    private int quantity;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Product(String name, Double price, String description, int quantity, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
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
