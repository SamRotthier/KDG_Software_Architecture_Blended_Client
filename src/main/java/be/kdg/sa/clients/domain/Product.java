package be.kdg.sa.clients.domain;

import be.kdg.sa.clients.domain.Enum.ProductState;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cl_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;
    private String name;
    private BigDecimal price;
    private String description;
    private int quantity;
    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orders;
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Product(UUID productId, String name, BigDecimal price, String description, int quantity, LocalDateTime createdDate, LocalDateTime modifiedDate, ProductState productState) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.productState=productState;
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

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
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
        if(name == null || name.equals("")){
            throw new IllegalArgumentException("Enter a name.");
        }
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDescription(String description) {
        if(description == null || description.equals("")){
            throw new IllegalArgumentException("Enter a description.");
        }
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

    public void setOrders(List<OrderProduct> orders) {
        this.orders = orders;
    }

    public void setProductState(ProductState productState) {
        this.productState = productState;
    }
}