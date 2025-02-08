package org.example.api;

import io.restassured.response.Response;
import org.example.specification.ApiRequestSpecification;

import static io.restassured.RestAssured.given;


public class PostRequestUserApi {
    public static <T> Response post(T userDTO, String path) {
        return given()
                .spec(ApiRequestSpecification.getSpecification())
                .body(userDTO)
                .when()
                .post(path)
                .then()
                .extract()
                .response();
    }

    public static <T> Response post(T userDTO, String path, String token) {
        return given()
                .spec(ApiRequestSpecification.getSpecification())
                .header("Authorization",token)
                .body(userDTO)
                .when()
                .post(path)
                .then()
                .extract()
                .response();
    }
}
