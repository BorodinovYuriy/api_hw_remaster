package org.example.data;


import com.github.javafaker.Faker;
import org.example.dto.AuthUser.AuthRequestDTO;
import org.example.helpers.PropertiesLoader;

import java.util.HashMap;
import java.util.Map;

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

    public static Map<String,String> makeRegisteredUser(){
        Map<String,String> user = new HashMap<>();
        user.put("username", PropertiesLoader.getUsername());
        user.put("password", PropertiesLoader.getPassword());
        return user;
    }

    public static AuthRequestDTO makeAuthRequestDTO(){
        AuthRequestDTO user = new AuthRequestDTO();
        user.setUsername(PropertiesLoader.getUsername());
        user.setPassword(PropertiesLoader.getPassword());
        return user;
    }







}