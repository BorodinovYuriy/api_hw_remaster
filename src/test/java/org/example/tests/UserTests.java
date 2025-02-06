package org.example.tests;

import io.restassured.response.Response;
import org.bson.Document;
import org.example.data.Data;
import org.example.dto.UserAuthDTO;
import org.example.helpers.Converter;
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

        Document userDocument = mongo.getUserDocumentById(
                PropertiesLoader.getMongoCollectionUsers(),
                response.jsonPath().getInt("user._id"));

        Assert.assertEquals(
                response.jsonPath().getObject("user", UserAuthDTO.class).toString(),
                Converter.convertJsonToDto(userDocument.toJson(),UserAuthDTO.class).toString(),
                "Основные поля в response и ответа от mongo - не совпадают!");

        token = response.jsonPath().getString("token");
    }
    @Test(description = "Добавление нового пользавателя",dependsOnMethods = "canGetUserByLogin")
    void canAddUser(){
        System.out.println("Выполнение теста 2");

    }
    @Test(description = "Добавление вопроса",dependsOnMethods = "canGetUserByLogin")
    public void canAddQuestion() {
        System.out.println("Выполнение теста 3");
    }

    @Test(description = "Редактирование вопроса",dependsOnMethods = "canGetUserByLogin")
    public void canEditQuestion() {
        System.out.println("Выполнение теста 4");
    }

    @Test(description = "Добавление квиза",dependsOnMethods = "canGetUserByLogin")
    public void canAddQuiz() {
        System.out.println("Выполнение теста 5");
    }

    @Test(description = "Добавление модуля",dependsOnMethods = "canGetUserByLogin")
    public void canAddModule() {
        System.out.println("Выполнение теста 6");
    }

    @Test(description = "Добавление курса",dependsOnMethods = "canGetUserByLogin")
    public void canAddCurse() {
        System.out.println("Выполнение теста 7");
    }

    @Test(description = "Добавление экзамена",dependsOnMethods = "canGetUserByLogin")
    public void canAddExam() {
        System.out.println("Выполнение теста 8");
    }

    @Test(description = "Добавление темплейта",dependsOnMethods = "canGetUserByLogin")
    public void canAddTemplate() {
        System.out.println("Выполнение теста 9");
    }

    @Test(description = "Авторизация с неверным логином или паролем",dependsOnMethods = "canGetUserByLogin")
    public void wrongCredential() {
        System.out.println("Выполнение теста 10");
    }
}
