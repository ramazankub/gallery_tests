package ru.gallery.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gallery.config.Config;
import ru.gallery.page.MainPage;
import ru.gallery.utils.AuthWebUtils;
import ru.gallery.utils.DataUtils;

import static ru.gallery.utils.DataUtils.DEFAULT_PASSWORD;
import static ru.gallery.utils.DataUtils.DEFAULT_USERNAME;

public class MuseumWebTest extends BaseTest {

    private static final Config CFG = Config.getInstance();

    // Авторизуемся перед каждым тестом. По-хорошему нужно создавать нового пользователя и авторизовываться через API,
    // и прокидывать токен в куки браузера, но для простоты так тоже пойдет
    @BeforeEach
    void authUser() {
        AuthWebUtils.authUser(DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    @Test
    @Description("Проверка добавления музея")
        // Описание теста для отчета в Allure
    void addMuseumShouldBeSuccess() {
        // Заранее генерим все данные, которые нам пригодятся
        String museumTitle = DataUtils.randomMuseumName();
        String country = DataUtils.defaultCountryName();
        String city = DataUtils.randomCityName();
        String museumImagePath = "img/museums/hermitage.jpg";
        String description = DataUtils.randomText();

        Selenide.open(CFG.frontUrl(), MainPage.class) // MainPage - страница, которая откроется первой
                .clickMuseumButton() // Кликаем на кнопку "Музеи". Метод возвращает страницу музея MuseumPage
                .checkElements()// И мы уже можем вызвать метод из MuseumPage, чтобы проверить наличие элементов
                .clickAddMuseumButton()
                .setMuseumNameInput(museumTitle)
                .setCountry(country)
                .setCityInput(city)
                .setMuseumPhotoInput(museumImagePath)
                .setDescriptionInput(description)
                .clickAddButton()
                .checkToastMuseumAdded(museumTitle)
                .findMuseum(museumTitle)
                .checkMuseumTitleAndPhoto(museumTitle, museumImagePath)
                .findMuseumOnPageAndClick(museumTitle)
                .checkMuseumTitle(museumTitle)
                .checkMuseumLocation(country, city)
                .checkMuseumDescription(description)
                .checkMuseumPhoto(museumImagePath);
    }
}
