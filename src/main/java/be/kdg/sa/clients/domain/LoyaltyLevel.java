package be.kdg.sa.clients.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "cl_loyalty_level")
public class LoyaltyLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double discount;
    private int minPoints;
    private int maxPoints;

    public LoyaltyLevel(String name, double discount, int minPoints, int maxPoints) {
        this.name = name;
        this.discount = discount;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
    }

    public LoyaltyLevel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public void setMinPoints(int minPoints) {
        this.minPoints = minPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
