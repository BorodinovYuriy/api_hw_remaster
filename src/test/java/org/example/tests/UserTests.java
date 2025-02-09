package org.example.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.bson.Document;
import org.example.api.PostRequestUserApi;
import org.example.data.DataProviders;
import org.example.dto.addfakeuser.AddFakeUserDTO;
import org.example.dto.addfakeuser.AddFakeUserDataDTO;
import org.example.dto.authuser.AuthRequestDTO;
import org.example.dto.authuser.AuthResponseDTO;
import org.example.dto.authuser.UserDTO;
import org.example.dto.questionadd.QuestionDTO;
import org.example.helpers.DocumentConverter;
import org.example.helpers.MongoDBHelper;
import org.example.helpers.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        Response response = PostRequestUserApi.post(user, "/api/auth/login");
        Assert.assertEquals(response.statusCode(),200, "Не ожидаемый статус-код!");
        Assert.assertTrue(response.getContentType().contains(ContentType.JSON.toString()),
                "Content-Type должен содержать application/json");

        Document userDocument = mongo.getDocInMongo(
                PropertiesLoader.getMongoCollectionUsers(),
                response.jsonPath().getInt("user._id"),
                "_id");

        UserDTO respUser = response.as(AuthResponseDTO.class).getUser();
        UserDTO mongoUser = DocumentConverter.convertDocumentToDTO(userDocument,UserDTO.class);

        Assert.assertNotNull(mongoUser,"mongoUser is null!");
        Assert.assertEquals(respUser, mongoUser, "Пользователь response user и mongo user не идентичны!");
        token = response.jsonPath().getString("token");
        logger.info("token получен: {}", token);
        logger.info("auth test - пройден");
    }

    @Test(
            description = "Добавление нового пользавателя",
            dependsOnMethods = "canGetUserByLogin",
            dataProvider = "fakeUserAdd",
            dataProviderClass = DataProviders.class
    )
    void canAddUser(AddFakeUserDTO user){
        Response response = PostRequestUserApi.post(user, "/api/user-auth1", token);
        Assert.assertEquals(response.statusCode(),200, "Не ожидаемый статус-код!");
        Assert.assertTrue(response.getContentType().contains(ContentType.JSON.toString()),
                "Content-Type должен содержать application/json");

        Document userDocument = mongo.getDocInMongo(
                PropertiesLoader.getMongoCollectionUsers(),
                response.jsonPath().getInt("data._id"),
                "_id");


        AddFakeUserDTO respUser = response.as(AddFakeUserDataDTO.class).getUser();
        AddFakeUserDTO mongoUser = DocumentConverter.convertDocumentToDTO(userDocument,AddFakeUserDTO.class);

        Assert.assertNotNull(mongoUser,"mongoUser is null!");
        Assert.assertEquals(respUser, mongoUser, "Пользователь response user и mongo user не идентичны!");
        logger.info("add new user - тест пройден.");
    }

    @Test(
            description = "Добавление и редактирование вопроса",
            dependsOnMethods = "canGetUserByLogin",
            dataProvider = "questionAddJsonFile",
            dataProviderClass = DataProviders.class
    )
    public void canAddQuestion(QuestionDTO question,File jsonFile) {
        Response response = PostRequestUserApi.post(question, "/api/theme-question", token);
        Assert.assertEquals(response.statusCode(),200, "Не ожидаемый статус-код!");
        Assert.assertTrue(response.getContentType().contains(ContentType.JSON.toString()),
                "Content-Type должен содержать application/json");

        Document userDocument = mongo.getDocInMongo(
                PropertiesLoader.mongoCollectionQuizzes(),
                response.jsonPath().getInt("data._id"),
                "question");

        Assert.assertEquals(
                response.jsonPath().getString("data.name"),
                userDocument.get("specialName").toString(),
                "Вопрос из response и вопрос из mongo - Не идентичны!"
        );
        logger.info("add question test - пройден.");
//---------------------------------------------------------------------------------------------

        int id = response.jsonPath().getInt("data._id");
        System.out.println("resp_1 data._id: "+id);
        String jsonString = null;

        try {
            jsonString = new String(Files.readAllBytes(Paths.get(jsonFile.getPath())));
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }

        // Меняем значение "question"
        String modifiedJsonString = null;
        String checkName = "checkingNameModifier";


        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonString);

            // Изменяем значение поля "question"
            if (rootNode instanceof ObjectNode) {
                ((ObjectNode) rootNode).put("question", id);
            }
            // Получаем узел "LTP"
            JsonNode ltpNode = rootNode.get("LTP");
            if (ltpNode instanceof ObjectNode) {
                // Получаем узел "data"
                JsonNode dataNode = ltpNode.get("data");
                if (dataNode instanceof ObjectNode) {

                    // Изменяем значение поля "name"
                    ((ObjectNode) dataNode).put("name", checkName);
                }
            }


            // Преобразуем обратно в JSON-строку
            modifiedJsonString = objectMapper.writeValueAsString(rootNode);
            System.out.println("modifiedJsonString:");
            System.out.println(modifiedJsonString);



        } catch (IOException e) {
            System.err.println("Error processing JSON: " + e.getMessage());
        }
        //------------------------222
        Response questionChangeResponse = PostRequestUserApi.post(modifiedJsonString, "/api/create-lts", token);
        Assert.assertEquals(questionChangeResponse.statusCode(),200, "Не ожидаемый статус-код!");

        Document questionDocument = mongo.getDocInMongo(
                PropertiesLoader.mongoCollectionQuizzes(),
                response.jsonPath().getInt("data._id"),
                "question");

        Assert.assertEquals(
                checkName,
                questionDocument.get("specialName").toString(),
                "Вопрос из response и вопрос из mongo - Не идентичны!"
        );

    }



























}
