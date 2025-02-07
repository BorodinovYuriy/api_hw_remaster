package org.example.tests;

import io.restassured.response.Response;
import org.bson.Document;
import org.example.data.Data;
import org.example.dto.AuthUser.AuthResponseDTO;
import org.example.dto.AuthUser.UserDTO;
import org.example.helpers.DocumentConverter;
import org.example.helpers.MongoDBHelper;
import org.example.helpers.PropertiesLoader;
import org.example.tests.api.AuthApiClient;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserTests {
    private String token;
    private MongoDBHelper mongo;

    @BeforeClass
    void setup(){
        mongo = new MongoDBHelper();
    }
    @AfterClass
    protected void teardown(){
        if(mongo != null){
            mongo.close();
        }
    }

    @Test(description = "Авторизация на портале", priority = 0)
    public void canGetUserByLogin() {

        Response response = AuthApiClient.login(Data.makeRegisteredUser());
        Assert.assertEquals(response.statusCode(),200, "Код ответа НЕ 200!");

        Document userDocument = mongo.getUserDocumentById(
                PropertiesLoader.getMongoCollectionUsers(),
                response.jsonPath().getInt("user._id"));

        Assert.assertEquals(
                response.as(AuthResponseDTO.class).getUser(),
                DocumentConverter.convertDocumentToDTO(userDocument,UserDTO.class),
                "Пользователь response user и mongo user не идентичны!"
        );

        token = response.jsonPath().getString("token");
    }

}
