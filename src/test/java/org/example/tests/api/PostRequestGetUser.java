package org.example.tests.api;

import io.restassured.response.Response;
import org.example.dto.AuthUser.AuthRequestDTO;
import org.example.tests.specification.ApiRequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class PostRequestGetUser {

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
