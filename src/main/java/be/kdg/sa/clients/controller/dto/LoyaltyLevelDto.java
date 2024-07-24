package be.kdg.sa.clients.controller.dto;

public class LoyaltyLevelDto {
    private String name;
    private double discount;
    private int minPoints;
    private int maxPoints;

    public LoyaltyLevelDto() {
    }

    public LoyaltyLevelDto(String name, double discount, int minPoints, int maxPoints) {
        this.name = name;
        this.discount = discount;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
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
}
