package org.example.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class Converter {

    public static <T> T convertJsonToDto(String jsonString, Class<T> dtoClass) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, dtoClass);
        } catch (IOException e) {
            System.err.println("Ошибка при преобразовании JSON в DTO: " + e.getMessage());
            throw new RuntimeException("Ошибка при преобразовании JSON в DTO", e);
        }
    }
}
