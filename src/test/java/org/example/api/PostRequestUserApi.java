package org.example.api;

import io.restassured.response.Response;
import org.example.specification.ApiRequestSpecification;

import static io.restassured.RestAssured.given;


public class PostRequestUserApi {
    public static <T> Response post(T data, String path) {
        return given()
                .spec(ApiRequestSpecification.getSpecification())
                .body(data)
                .when()
                .post(path)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    public static <T> Response post(T data, String path, String token) {
        return given()
                .spec(ApiRequestSpecification.getSpecification())
                .header("Authorization",token)
                .body(data)
                .when()
                .post(path)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
