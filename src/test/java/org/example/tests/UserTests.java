package org.example.tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.bson.Document;
import org.example.api.PostRequestUserApi;
import org.example.data.DataProviders;
import org.example.dto.addfakeuser.AddFakeUserDTO;
import org.example.dto.addfakeuser.AddFakeUserDataDTO;
import org.example.dto.addmodule.AddModuleDTO;
import org.example.dto.authuser.AuthRequestDTO;
import org.example.dto.authuser.AuthResponseDTO;
import org.example.dto.authuser.UserDTO;
import org.example.dto.questionadd.QuestionDTO;
import org.example.helpers.DocumentConverter;
import org.example.helpers.JSONHelper;
import org.example.helpers.MongoDBHelper;
import org.example.helpers.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
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
        Assert.assertTrue(response.getContentType().contains(ContentType.JSON.toString()),
                "Content-Type должен содержать application/json");

        Document userDocument = mongo.getDocQueryInMongo(
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
        Assert.assertTrue(response.getContentType().contains(ContentType.JSON.toString()),
                "Content-Type должен содержать application/json");

        Document userDocument = mongo.getDocQueryInMongo(
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
            dataProvider = "questionAndJson",
            dataProviderClass = DataProviders.class
    )
    public void canAddAndEditQuestion(QuestionDTO question, File jsonFile, String checkName) {
        Response response = PostRequestUserApi.post(question, "/api/theme-question", token);
        Assert.assertTrue(response.getContentType().contains(ContentType.JSON.toString()),
                "Content-Type должен содержать application/json");

        Document userDocument = mongo.getDocQueryInMongo(
                PropertiesLoader.getMongoCollectionQuizzes(),
                response.jsonPath().getInt("data._id"),
                "question");

        Assert.assertEquals(
                response.jsonPath().getString("data.name"),
                userDocument.get("specialName").toString(),
                "Вопрос из response и вопрос из mongo - Не идентичны!"
        );
        logger.info("add question test - пройден.");

        String jsonString = JSONHelper.fileToJSON(Paths.get(jsonFile.getPath()));
        Response questionChangeResponse = PostRequestUserApi.post(
                JSONHelper.prepareJsonQuestion(jsonString, response.jsonPath().getInt("data._id"), checkName),
                "/api/create-lts",
                token
        );
        Assert.assertEquals(questionChangeResponse.statusCode(),200, "Не ожидаемый статус-код!");

        Document questionDocument = mongo.getDocQueryInMongo(
                PropertiesLoader.getMongoCollectionQuizzes(),
                response.jsonPath().getInt("data._id"),
                "question");

        Assert.assertEquals(
                checkName,
                questionDocument.get("specialName").toString(),
                "Вопрос из response и вопрос из mongo - Не идентичны!"
        );
        logger.info("edit question test - пройден.");
    }

    @Test(
            description = "Добавление квиза",
            dependsOnMethods = "canGetUserByLogin",
            dataProvider = "addQuiz",
            dataProviderClass = DataProviders.class
    )
    public void canAddQuiz(File jsonFile) {
        Response response = PostRequestUserApi.post(
                JSONHelper.fileToJSON(Paths.get(jsonFile.getPath())),
                "/api/quiz",
                token
        );

        //В ТЗ сказано: -"Проверить заголовки", но почему то дана БД...
        Assert.assertTrue(response.getContentType().contains(ContentType.JSON.toString()));
        Assert.assertNotNull(response.getHeader("Date"), "Заголовок Date отсутствует!");
        Assert.assertNotNull(response.getHeader("Connection"), "Заголовок Connection отсутствует!");
        Assert.assertNotNull(response.getHeader("Vary"), "Заголовок Vary отсутствует!");
        Assert.assertNotNull(response.getHeader("ETag"), "Заголовок ETag отсутствует!");

        Document document = mongo.getDocQueryInMongo(
                PropertiesLoader.getMongoCollectionQuizzes(),
                response.jsonPath().getInt("data._id"),
                "_id"
        );

        Assert.assertNotNull(document,"Документ не найден в базе mongo!");

        logger.info("add quiz test - пройден.");
    }

    @Test(
            description = "Добавление модуля",
            dependsOnMethods = "canGetUserByLogin",
            dataProvider = "addModule",
            dataProviderClass = DataProviders.class
    )
    public void canAddModule(AddModuleDTO moduleDTO) {
        Response response = PostRequestUserApi.post(moduleDTO, "/api/course-module", token);

        Document document = mongo.getDocQueryInMongo(
                PropertiesLoader.getMongoCollectionCourseModules(),
                response.jsonPath().getInt("data._id"),
                "_id"
                );

        Assert.assertEquals(
                moduleDTO,
                DocumentConverter.convertDocumentToDTO(document,AddModuleDTO.class),
        "response dto и document dto в mongo - не идентичны!");




        logger.info("add module test - пройден.");

    }

    @Test(
            description = "Добавление курса",
            dependsOnMethods = "canGetUserByLogin",
            dataProvider = "addCourse",
            dataProviderClass = DataProviders.class
    )
    public void canAddCurse(File jsonFile) {
        Response response = PostRequestUserApi.post(
                JSONHelper.fileToJSON(Paths.get(jsonFile.getPath())),
                "/api/course",
                token);
        Assert.assertNotNull(response.jsonPath().getInt("data._id"),
                "Поле data._id в response не должно быть Null");

        Assert.assertEquals(
                response.jsonPath().getInt("data._id"),
                mongo.getDocQueryInMongo(
                        PropertiesLoader.getMongoCollectionCourses(),
                        response.jsonPath().getInt("data._id"),
                        "_id"
                ).getInteger("_id"),
                "Неудачное сравнение полей 'id' из response и mongo"
                );

        logger.info("add course test - пройден.");
    }
    @Test(
            description = "Добавление экзамена",
            dependsOnMethods = "canGetUserByLogin",
            dataProvider = "addExam",
            dataProviderClass = DataProviders.class
    )
    public void canAddExam(File jsonFile) {
        Response response = PostRequestUserApi.post(
                JSONHelper.fileToJSON(Paths.get(jsonFile.getPath())),
                "/api/exam",
                token);
        Assert.assertNotNull(response.jsonPath().getInt("data._id"),
                "Поле data._id в response не должно быть Null");

        Assert.assertEquals(
                response.jsonPath().getInt("data._id"),
                mongo.getDocQueryInMongo(
                        PropertiesLoader.getMongoCollectionExams(),
                        response.jsonPath().getInt("data._id"),
                        "_id"
                ).getInteger("_id"),
                "Неудачное сравнение полей 'id' из response и mongo"
        );
        logger.info("add exam test - пройден.");
    }
    @Test(
            description = "Добавление темплейта",
            dependsOnMethods = "canGetUserByLogin",
            dataProvider = "addTemplate",
            dataProviderClass = DataProviders.class
    )
    public void canAddTemplate(File jsonFile) {
        Response response = PostRequestUserApi.post(
                JSONHelper.fileToJSON(Paths.get(jsonFile.getPath())),
                "/api/user-hr-template",
                token);

        Assert.assertEquals(
                response.jsonPath().getInt("data._id"),
                mongo.getDocQueryInMongo(
                        PropertiesLoader.getMongoCollectionTemplates(),
                        response.jsonPath().getInt("data._id"),
                        "_id"
                ).getInteger("_id"),
                "Неудачное сравнение полей 'id' из response и mongo"
        );
        logger.info("add template test - пройден.");



    }



























}
