package org.example.dto.AuthUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthRequestDTO {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

}
