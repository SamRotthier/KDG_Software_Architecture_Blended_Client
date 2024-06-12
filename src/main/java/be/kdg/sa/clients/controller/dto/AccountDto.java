package be.kdg.sa.clients.controller.dto;

import be.kdg.sa.clients.domain.Enum.AccountRelationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AccountDto {
    @NotNull
    @NotEmpty
    private String lastName;
    @NotNull
    @NotEmpty
    private String firstName;
    @Email
    @NotNull
    @NotEmpty
    private String email;

    // @NotBlank
    // private String password;

    private String company;

    private AccountRelationType type;

    public AccountDto() {
    }

    public AccountDto(String lastName, String firstName, String email, String company, AccountRelationType type) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.company = company;
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

    public AccountRelationType getType() {
        return type;
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

    public void setType(AccountRelationType type) {
        this.type = type;
    }

    /*public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }*/
}
