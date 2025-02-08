package org.example.dto.addfakeuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddFakeUserDataDTO {
    @JsonProperty("data")
    AddFakeUserDTO user;

    public AddFakeUserDataDTO() {
    }

    public AddFakeUserDTO getUser() {
        return user;
    }
}
