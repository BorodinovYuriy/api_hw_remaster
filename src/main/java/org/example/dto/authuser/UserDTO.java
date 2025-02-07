package org.example.dto.authuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    public UserDTO() {
    }

    @JsonProperty("_id")
    private String id;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("roles")
    private List<String> roles;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(surname, userDTO.surname) && Objects.equals(firstName, userDTO.firstName) && Objects.equals(username, userDTO.username) && Objects.equals(roles, userDTO.roles) && Objects.equals(name, userDTO.name) && Objects.equals(email, userDTO.email) && Objects.equals(password, userDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, firstName, username, roles, name, email, password);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", surname='" + surname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

