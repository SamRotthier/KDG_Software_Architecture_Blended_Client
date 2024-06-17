package be.kdg.sa.clients.controller.dto;

import be.kdg.sa.clients.domain.Enum.LoyaltyLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.UUID;

public class LoyaltyDto {
    private UUID accountId;
    private String lastName;
    private String firstName;
    private int points;
    private LoyaltyLevel loyaltyLevel;

    public LoyaltyDto() {
    }

    public LoyaltyDto(UUID accountId, String lastName, String firstName, int points, LoyaltyLevel loyaltyLevel) {
        this.accountId = accountId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.points = points;
        this.loyaltyLevel = loyaltyLevel;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LoyaltyLevel getLoyaltyLevel() {
        return loyaltyLevel;
    }

    public void setLoyaltyLevel(LoyaltyLevel loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel;
    }
}
