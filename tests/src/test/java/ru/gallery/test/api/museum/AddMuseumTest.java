package ru.gallery.test.api.museum;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class AddMuseumTest {

    @Test
    @Description("Проверка корректного добавления музея")
    void addMuseumTest() {
    }

    @ParameterizedTest
    @ValueSource(strings = {"впиши сюда значения"})
    @Description("Проверка ошибки при отсутствии или некорректном токене")
    void addMuseumWithIncorrectToken(String token) {
    }

    @Test
    @Description("Проверка ошибки при добавлении музея без названия")
    void addMuseumWithoutTitle() {
    }

    @Test
    @Description("Проверка ошибки при добавлении музея без описания")
    void addMuseumWithoutDescription() {
    }

    @Test
    @Description("Проверка ошибки при добавлении музея без фото")
    void addMuseumWithoutPhoto() {
    }

    @Test
    @Description("Проверка ошибки при добавлении музея без местоположения")
    void addMuseumWithoutGeo() {
    }

    @Test
    @Description("Проверка ошибки при добавлении музея с несуществующим id страны")
    void addMuseumWithIncorrectCountryId() {
    }

}
