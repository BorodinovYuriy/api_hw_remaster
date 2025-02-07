package org.example.dto.AuthUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponseDTO {
    @JsonProperty("token")
    private String token;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("user")
    private UserDTO user;
}
