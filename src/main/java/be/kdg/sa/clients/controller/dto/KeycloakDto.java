package be.kdg.sa.clients.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class KeycloakDto {
    private String lastName;
    private String firstName;
    private String username;
    private String email;

    private boolean enabled;
    private List<Credential> credentials;

    public KeycloakDto(String lastName, String firstName, String username, String password, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.username = username;
        this.email = email;
        this.credentials = List.of(new Credential(password));
        this.enabled = true;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.credentials = List.of(new Credential(password));}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }


    public static class Credential {
        private String type;
        private String value;
        private boolean temporary;

        public Credential(String value) {
            this.type = "password";
            this.value = value;
            this.temporary = false;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isTemporary() {
            return temporary;
        }

        public void setTemporary(boolean temporary) {
            this.temporary = temporary;
        }
    }
}
