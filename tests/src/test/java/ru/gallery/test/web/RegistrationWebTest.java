package ru.gallery.test.web;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.gallery.config.Config;
import ru.gallery.utils.AuthWebUtils;
import ru.gallery.utils.DataUtils;

import java.util.stream.Stream;

import static ru.gallery.utils.DataUtils.DEFAULT_PASSWORD;
import static ru.gallery.utils.DataUtils.DEFAULT_USERNAME;

public class RegistrationWebTest extends BaseTest {

    private static final Config CFG = Config.getInstance();

    static Stream<String> shouldBeRegisterNewUser() {
        // Делаем стрим из двух корректных значений: 3 (минимально допустимое) и 50 (максимально допустимое)
        // Тест прогонится столько раз, сколько данных сгенерит этот метод. В данном случае два раза
        // Каждое из сгенеренных значений будет попадать в параметр username
        return Stream.of(DataUtils.randomCharactersInQuantity(3), DataUtils.randomCharactersInQuantity(50));
    }

    static Stream<String> shouldBeErrorWhenIncorrectUsername() {
        return Stream.of();
    }

    static Stream<String> shouldBeErrorWhenIncorrectPasswordAndRepeatPassword() {
        return Stream.of();
    }

    @BeforeEach
    void authUser() {
        AuthWebUtils.authUser(DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    @ParameterizedTest
    @MethodSource()
    // Эта аннотация вызывает метод с названием, аналогичным названию теста. Можно в скобках написать кастомное название
    @Description("Проверка регистрации нового пользователя")
    void shouldBeRegisterNewUser(String username) {
        // Твой код
    }

    @Test
    @Description("Проверка ошибки при регистрации нового пользователя с уже существующим именем")
    void shouldBeErrorWhenRegisterWithExistUsername() {
        // Твой код
    }

    @ParameterizedTest // Проверяем граничные значения снизу и сверху
    @MethodSource()
    @Description("Проверка ошибки при регистрации нового пользователя с некорректным именем")
    void shouldBeErrorWhenIncorrectUsername(String username) {
        // Твой код
    }

    @ParameterizedTest // Проверяем граничные значения снизу и сверху
    @MethodSource()
    @Description("Проверка ошибки при регистрации нового пользователя с некорректным именем")
    void shouldBeErrorWhenIncorrectPasswordAndRepeatPassword(String password) {
        // Твой код
    }
}
