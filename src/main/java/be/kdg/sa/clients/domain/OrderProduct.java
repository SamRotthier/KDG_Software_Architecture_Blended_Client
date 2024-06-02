package be.kdg.sa.clients.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "cl_order_product")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;
    private int quantity;

    public OrderProduct() {
    }

    public OrderProduct(Order order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public OrderProduct(UUID id, Order order, int quantity) {
        this.id = id;
        this.order = order;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
