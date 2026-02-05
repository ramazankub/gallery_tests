package ru.gallery.test.api.geo;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class GetAllCountriesTest {

    @Test
    @Description("Проверка корректного получения всех стран")
    void getAllCountriesTest() {
        // Отправляем запрос, идем в базу, сравниваем получившееся между собой
    }

    @ParameterizedTest
    @ValueSource(strings = {"впиши сюда значения"})
    @Description("Проверка ошибки при отсутствии или некорректном токене")
    void getAllCountriesWithIncorrectToken(String token) {
    }
}
