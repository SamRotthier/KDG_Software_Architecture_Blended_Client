package be.kdg.sa.clients.domain;

import be.kdg.sa.clients.domain.Enum.AccountRelationType;

// @Entity
public class Account {

    private String lastName;
    private String firstName;
    private String email;
    private String company;
    private int points;

    private AccountRelationType type;

    public Account(String lastName, String firstName, String email, String company, int points, AccountRelationType type) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.company = company;
        this.points = points;
        this.type = type;
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

    public void setLastName(String lastName) {
        if(firstName == null || firstName.equals("")){
            throw new IllegalArgumentException("Enter a name.");
        }
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        if(firstName == null || firstName.equals("")){
            throw new IllegalArgumentException("Enter a name.");
        }
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPoints(int points) {
        if(points < 0){
            throw new IllegalArgumentException("Enter a valid amount of points.");
        }
        this.points = points;
    }

    public void setType(AccountRelationType type) {
        if(type == null){
            throw new IllegalArgumentException("No type account relation type was declared");
        }
        this.type = type;
    }
}
