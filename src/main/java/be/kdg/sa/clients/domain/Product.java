package be.kdg.sa.clients.domain;

import be.kdg.sa.clients.domain.Enum.ProductState;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cl_product")
public class Product {
    @Id
    private UUID productId;
    private String name;
    private BigDecimal price;
    private String description;
    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orders;
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private int orderCounter;

    public Product(UUID productId, String name, BigDecimal price, String description, LocalDateTime createdDate, ProductState productState) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.createdDate = createdDate;
        this.productState=productState;
        this.orderCounter = 0;
    }

    public Product() {

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderProduct> getOrders() {
        return orders;
    }

    public ProductState getProductState() {
        return productState;
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

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setOrders(List<OrderProduct> orders) {
        this.orders = orders;
    }

    public void setProductState(ProductState productState) {
        this.productState = productState;
    }

    public int getOrderCounter() {
        return orderCounter;
    }

    public void setOrderCounter(int orderCounter) {
        this.orderCounter = orderCounter;
    }
}