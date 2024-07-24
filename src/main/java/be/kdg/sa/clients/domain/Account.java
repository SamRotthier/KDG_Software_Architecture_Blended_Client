package be.kdg.sa.clients.domain;

import be.kdg.sa.clients.domain.Enum.AccountRelationType;
import be.kdg.sa.clients.domain.LoyaltyLevel;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cl_account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;
    private String lastName;
    private String firstName;
    private String email;
    private String company;
    private int points;

    @ManyToOne
    @JoinColumn(name = "loyalty_level_id")
    private LoyaltyLevel loyaltyLevel;

    @Enumerated(EnumType.STRING)
    private AccountRelationType type;

    @OneToMany(mappedBy = "account")
    private List<Order> orders;

    public Account() {
    }

    public Account(UUID accountId, String lastName, String firstName, String email, String company, int points, AccountRelationType type, LoyaltyLevel loyaltyLevel) {
        this.accountId = accountId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.company = company;
        this.points = points;
        this.type = type;
        this.loyaltyLevel = loyaltyLevel;
    }


    public UUID getAccountId() {
        return accountId;
    }
    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }

    public int getPoints() {
        return points;
    }

    public AccountRelationType getType() {
        return type;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setType(AccountRelationType type) {
        this.type = type;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public LoyaltyLevel getLoyaltyLevel() {
        return loyaltyLevel;
    }

    public void setLoyaltyLevel(LoyaltyLevel loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel;
    }
}
