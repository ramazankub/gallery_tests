package ru.gallery.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.ResponseBody;

import java.io.IOException;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Этот метод нужен для того, чтобы преобразовать "сырой" ответ в нужный json.
    public static <T> T readBody(ResponseBody body, Class<T> clazz) {
        try {
            return objectMapper.readValue(body.byteStream(), clazz);
        } catch (IOException e) {
            throw new AssertionError("Ошибка десериализации ответа", e);
        }
    }
}
