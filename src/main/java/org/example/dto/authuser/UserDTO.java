package org.example.dto.authuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

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


}

