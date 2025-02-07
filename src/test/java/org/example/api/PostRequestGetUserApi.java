package org.example.api;

import io.restassured.response.Response;
import org.example.dto.authuser.AuthRequestDTO;
import org.example.specification.ApiRequestSpecification;

import static io.restassured.RestAssured.given;

public class PostRequestGetUserApi {

    public static Response get(AuthRequestDTO user, String path) {
        return given()
                .spec(ApiRequestSpecification.getSpecification())
                .body(user)
                .when()
                .post(path)
                .then()
                .extract()
                .response();
    }
}
