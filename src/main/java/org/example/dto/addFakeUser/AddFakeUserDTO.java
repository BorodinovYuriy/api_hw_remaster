package org.example.dto.addFakeUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddFakeUserDTO {
    @JsonProperty("first_name")
    String first_name;
    @JsonProperty("surname")
    String surname;
    @JsonProperty("email")
    String email;
    @JsonProperty("username")
    String username;
    @JsonProperty("plain_password")
    String plain_password;

    public AddFakeUserDTO() {
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlain_password() {
        return plain_password;
    }

    public void setPlain_password(String plain_password) {
        this.plain_password = plain_password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AddFakeUserDTO that = (AddFakeUserDTO) o;
        return Objects.equals(first_name, that.first_name)
                && Objects.equals(surname, that.surname) && Objects.equals(email, that.email)
                && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first_name, surname, email, username);
    }

    @Override
    public String toString() {
        return "AddFakeUserDTO{" +
                "first_name='" + first_name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", plain_password='" + plain_password + '\'' +
                '}';
    }
}
