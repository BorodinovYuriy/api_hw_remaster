package org.example.data;


import com.github.javafaker.Faker;
import org.example.dto.addfakeuser.AddFakeUserDTO;
import org.example.dto.authuser.AuthRequestDTO;
import org.example.dto.questionadd.QuestionDTO;
import org.example.helpers.PropertiesLoader;
import org.testng.annotations.DataProvider;

import java.io.File;

public class DataProviders {
    static Faker faker = new Faker();

    public static String makeUsername() {
        return faker.name().username();
    }
    public static String makePassword() {
        return faker.internet().password();
    }
    public static String makeName() {
        return faker.name().name();
    }
    public static String makeSurname() {
        return faker.name().lastName();
    }
    public static String makeEmail() {
        return faker.internet().emailAddress();
    }
    public static String makeQuestion() {
        return faker.lorem().sentence(5);
    }

    @DataProvider(name = "realUser")
    public static Object[][] realUser(){
        AuthRequestDTO user = new AuthRequestDTO();
        user.setUsername(PropertiesLoader.getUsername());
        user.setPassword(PropertiesLoader.getPassword());
        return new  Object[][] {{user}};
    }

    @DataProvider(name = "fakeUserAdd")
    public static Object[][] fakeUserAdd() {
        AddFakeUserDTO user = new AddFakeUserDTO();
        user.setFirst_name(makeName());
        user.setSurname(makeSurname());
        user.setUsername(makeUsername());
        user.setEmail(makeEmail());
        user.setPlain_password(makePassword());
        return new Object[][]{{user}};
    }

    @DataProvider(name = "questionAdd")
    public static Object[][] questionAdd() {
        QuestionDTO question = new QuestionDTO();
        question.setName(makeQuestion());
        return new Object[][]{{question}};
    }

    @DataProvider(name = "editData")
    public static Object[][] editData() {
        File jsonFile = new File("src/test/resources/jsons/editquestion.json");
        return new Object[][]{{jsonFile}};
    }

    @DataProvider
    public static Object[][] questionAndJson() {
        QuestionDTO question = new QuestionDTO();
        question.setName(makeQuestion());
        File jsonFile = new File("src/test/resources/jsons/editquestion.json");
        return new Object[][]{
                {question, jsonFile}
        };
    }
}