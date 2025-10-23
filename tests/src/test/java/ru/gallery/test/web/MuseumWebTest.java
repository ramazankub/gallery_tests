package ru.gallery.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import ru.gallery.config.Config;
import ru.gallery.page.MainPage;
import ru.gallery.utils.AuthWebUtils;
import ru.gallery.utils.DataUtils;

import static ru.gallery.utils.DataUtils.DEFAULT_PASSWORD;
import static ru.gallery.utils.DataUtils.DEFAULT_USERNAME;

public class MuseumWebTest extends BaseTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void addMuseumShouldBeSuccess() {
        String museumTitle = DataUtils.randomMuseumName();
        String country = DataUtils.defaultCountryName();
        String city = DataUtils.randomCityName();
        String museumImagePath = "img/museums/hermitage.jpg";
        String description = DataUtils.randomText();

        AuthWebUtils.authUser(DEFAULT_USERNAME, DEFAULT_PASSWORD);

        Selenide.open(CFG.frontUrl(), MainPage.class)
                .clickMuseumButton()
                .checkElements()
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
