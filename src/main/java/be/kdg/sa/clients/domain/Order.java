package be.kdg.sa.clients.domain;

import be.kdg.sa.clients.domain.Enum.OrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cl_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    @OneToMany(mappedBy = "order")
    private List<OrderProduct> products;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private BigDecimal totalPrice;
    @CreationTimestamp
    private LocalDateTime creationDateTime;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;


    public Order(UUID orderId, List<OrderProduct> products, OrderStatus status, BigDecimal totalPrice, LocalDateTime creationDateTime, Account account) {
        this.orderId = orderId;
        this.products = products;
        this.status = status;
        this.totalPrice = totalPrice;
        this.creationDateTime = creationDateTime;
        this.account = account;
    }
    public Order() {

    }

    public UUID getOrderId() {
        return orderId;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public Account getAccount() {
        return account;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}