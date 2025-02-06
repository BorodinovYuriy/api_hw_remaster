package org.example.tests.api;

import io.restassured.response.Response;
import org.example.dto.UserAuthDTO;
import org.example.tests.specification.ApiRequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthApiClient {

    public static Response login(Map<String,String> user) {
        return given()
                .spec(ApiRequestSpecification.getSpecification())
                .body(user)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
