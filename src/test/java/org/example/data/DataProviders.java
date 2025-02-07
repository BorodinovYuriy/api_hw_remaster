package org.example.data;


import com.github.javafaker.Faker;
import org.example.dto.addFakeUser.AddFakeUserDTO;
import org.example.dto.authuser.AuthRequestDTO;
import org.example.helpers.PropertiesLoader;
import org.testng.annotations.DataProvider;

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


    @DataProvider(name = "realUser")
    public static Object[][] makeRealUser(){
        AuthRequestDTO user = new AuthRequestDTO();
        user.setUsername(PropertiesLoader.getUsername());
        user.setPassword(PropertiesLoader.getPassword());
        return new  Object[][] {{user}};
    }

    @DataProvider(name = "fakeUserAdd")
    public Object[][] fakeUserAdd() {
        AddFakeUserDTO user = new AddFakeUserDTO();
        user.setFirst_name(makeName());
        user.setSurname(makeSurname());
        user.setUsername(makeUsername());
        user.setEmail(makeEmail());
        user.setPlain_password(makePassword());
        return new Object[][]{{user}};
    }
}