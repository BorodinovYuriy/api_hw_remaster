package org.example.specification;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.helpers.PropertiesLoader;

public class ApiRequestSpecification {
    public static RequestSpecification getSpecification(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(PropertiesLoader.getBaseURI());
        requestSpecBuilder.setContentType(ContentType.JSON);
        RequestSpecification spec = requestSpecBuilder.build();
        return spec;
    }
}
