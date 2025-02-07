package org.example.tests;

import io.restassured.response.Response;
import org.bson.Document;
import org.example.api.PostAddNewUserApi;
import org.example.data.DataProviders;
import org.example.dto.addFakeUser.AddFakeUserDTO;
import org.example.dto.addFakeUser.AddFakeUserDataDTO;
import org.example.dto.authuser.AuthRequestDTO;
import org.example.dto.authuser.AuthResponseDTO;
import org.example.dto.authuser.UserDTO;
import org.example.helpers.DocumentConverter;
import org.example.helpers.MongoDBHelper;
import org.example.helpers.PropertiesLoader;
import org.example.api.PostRequestGetUserApi;
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

    @Test(
            description = "Авторизация на портале",
            priority = 0,
            dataProvider = "realUser",
            dataProviderClass = DataProviders.class
    )
    public void canGetUserByLogin(AuthRequestDTO user) {
        Response response = PostRequestGetUserApi.get(user, "/api/auth/login");
        Assert.assertEquals(response.statusCode(),200, "Не ожидаемый статус-код!");

        Document userDocument = mongo.getUserDocumentById(
                PropertiesLoader.getMongoCollectionUsers(),
                response.jsonPath().getInt("user._id"));

        UserDTO respUser = response.as(AuthResponseDTO.class).getUser();
        UserDTO mongoUser = DocumentConverter.convertDocumentToDTO(userDocument,UserDTO.class);
        Assert.assertNotNull(mongoUser,"mongoUser is null!");
        logger.info("{} respUser:  {}", "canGetUserByLogin - ",respUser);
        logger.info("{} mongoUser: {}", "canGetUserByLogin - ",mongoUser);

        Assert.assertEquals(respUser, mongoUser, "Пользователь response user и mongo user не идентичны!");

        token = response.jsonPath().getString("token");
        logger.info("token получен: {}", token);
    }
    @Test(
            description = "Добавление нового пользавателя",
            dependsOnMethods = "canGetUserByLogin",
            dataProvider = "fakeUserAdd",
            dataProviderClass = DataProviders.class
    )
    void canAddUser(AddFakeUserDTO user){
        Response response = PostAddNewUserApi.get(user, "/api/user-auth1",token);
        Assert.assertEquals(response.statusCode(),200, "Не ожидаемый статус-код!");

        Document userDocument = mongo.getUserDocumentById(
                PropertiesLoader.getMongoCollectionUsers(),
                response.jsonPath().getInt("data._id"));


        AddFakeUserDTO respUser = response.as(AddFakeUserDataDTO.class).getUser();
        AddFakeUserDTO mongoUser = DocumentConverter.convertDocumentToDTO(userDocument,AddFakeUserDTO.class);
        Assert.assertNotNull(mongoUser,"mongoUser is null!");
        logger.info("{} respUser:  {}", "canAddUser - ", respUser);
        logger.info("{} mongoUser: {}", "canAddUser - ", mongoUser);

        Assert.assertEquals(respUser, mongoUser, "Пользователь response user и mongo user не идентичны!");
    }



























}
