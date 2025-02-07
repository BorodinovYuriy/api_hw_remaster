package org.example.api;

import io.restassured.response.Response;
import org.example.dto.addFakeUser.AddFakeUserDTO;
import org.example.specification.ApiRequestSpecification;

import static io.restassured.RestAssured.given;

public class PostAddNewUserApi {
    public static Response get(AddFakeUserDTO user, String path, String token) {
        return given()
                .spec(ApiRequestSpecification.getSpecification())
                .header("Authorization",token)
                .body(user)
                .when()
                .post(path)
                .then()
                .extract()
                .response();
    }
}
