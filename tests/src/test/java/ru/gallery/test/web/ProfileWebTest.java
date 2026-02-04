package ru.gallery.test.web;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gallery.config.Config;
import ru.gallery.utils.AuthWebUtils;

import static ru.gallery.utils.DataUtils.DEFAULT_PASSWORD;
import static ru.gallery.utils.DataUtils.DEFAULT_USERNAME;

public class ProfileWebTest extends BaseTest {

    private static final Config CFG = Config.getInstance();

    @BeforeEach
    void authUser() {
        AuthWebUtils.authUser(DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    @Test
    @Description("Проверка добавления данных в профиль")
    void profileInfoShouldBeAdded() {
        // Твой код
    }

    @Test
    @Description("Проверка редактирования уже добавленных данных в профиль")
    void profileInfoShouldBeChanged() {
        // Твой код
    }
}
