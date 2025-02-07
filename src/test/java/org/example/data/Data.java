package org.example.data;


import com.github.javafaker.Faker;
import org.example.dto.authuser.AuthRequestDTO;
import org.example.helpers.PropertiesLoader;

public class Data {
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

    public static AuthRequestDTO makeRealUser(){
        AuthRequestDTO user = new AuthRequestDTO();
        user.setUsername(PropertiesLoader.getUsername());
        user.setPassword(PropertiesLoader.getPassword());
        return user;
    }







}