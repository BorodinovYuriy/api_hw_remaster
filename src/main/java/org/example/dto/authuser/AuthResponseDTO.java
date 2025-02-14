package org.example.dto.authuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponseDTO {
    @JsonProperty("token")
    private String token;
    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("user")
    private UserDTO user;

    public AuthResponseDTO() {
    }

    public UserDTO getUser() {
        return user;
    }
}
