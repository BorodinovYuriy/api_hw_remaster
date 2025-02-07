package org.example.tests;

import io.restassured.response.Response;
import org.bson.Document;
import org.example.data.Data;
import org.example.dto.AuthUser.AuthResponseDTO;
import org.example.dto.AuthUser.UserDTO;
import org.example.helpers.DocumentConverter;
import org.example.helpers.MongoDBHelper;
import org.example.helpers.PropertiesLoader;
import org.example.tests.api.PostRequestGetUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserTests {
    private static final Logger logger = LoggerFactory.getLogger(UserTests.class);
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
        Response response = PostRequestGetUser.get(Data.makeAuthRequestDTO(), "/api/auth/login");
        Assert.assertEquals(response.statusCode(),200, "Не ожидаемый статус-код!");

        Document userDocument = mongo.getUserDocumentById(
                PropertiesLoader.getMongoCollectionUsers(),
                response.jsonPath().getInt("user._id"));

        UserDTO respUser = response.as(AuthResponseDTO.class).getUser();
        UserDTO mongoUser = DocumentConverter.convertDocumentToDTO(userDocument,UserDTO.class);
        Assert.assertNotNull(mongoUser,"mongoUser is null!");
        logger.info("respUser:  {}", respUser);
        logger.info("mongoUser: {}", mongoUser);

        Assert.assertEquals(respUser, mongoUser, "Пользователь response user и mongo user не идентичны!");

        token = response.jsonPath().getString("token");
        logger.info("token получен: {}", token);
    }
    @Test(description = "Добавление нового пользавателя",dependsOnMethods = "canGetUserByLogin")
    void canAddUser(){
        System.out.println("Выполнение теста 2");

    }



























}
