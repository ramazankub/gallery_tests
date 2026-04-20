package ru.gallery.test.web;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gallery.config.Config;
import ru.gallery.page.ArtistPage;
import ru.gallery.page.element.HeaderElement;
import ru.gallery.utils.AuthWebUtils;
import ru.gallery.utils.DataUtils;

import static ru.gallery.utils.DataUtils.DEFAULT_PASSWORD;
import static ru.gallery.utils.DataUtils.DEFAULT_USERNAME;

public class ArtistWebTest extends BaseTest {

    private final HeaderElement headerPage = new HeaderElement();
    private final ArtistPage artistPage = new ArtistPage();

    @BeforeEach
    void authUser() {
        AuthWebUtils.authUser(DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    @Test
    @Description("Проверка добавления художника")
    void addArtistShouldBeSuccess() {
        String artistName = DataUtils.randomArtistName();
        String path = "img/artists/botticelli.jpg";
        String bio =  DataUtils.randomText();

        headerPage.artistsButtonClick();
        artistPage.checkAddArtisBtnIsVisible();
        artistPage.clickAddArtistBtn();
        artistPage.checkAddArtistModalWindowIsVisible();
        artistPage.setArtistName(artistName);
        artistPage.setArtistPhoto(path);
        artistPage.setArtistBiography(bio);
        artistPage.checkModalBtnsAreVisible();
        artistPage.clickConfirmAddingBtn();

        artistPage.checkToastArtistAdded(artistName);

        artistPage.findArtist(artistName);
        artistPage.startSearch();

        artistPage.openArtist(artistName);
        artistPage.checkArtistName(artistName);
        artistPage.checkArtistBio(bio);
        artistPage.checkArtistPhoto();
    }

    @Test
    @Description("Проверка изменения уже созданного художника")
    void editArtistShouldBeSuccess() {
        String artistName = DataUtils.randomArtistName();
        String editedArtistName = DataUtils.randomArtistName();
        String path = "img/artists/botticelli.jpg";
        String editPath = "img/artists/da_vinci.jpg";
        String bio =  DataUtils.randomText();
        String editBio = DataUtils.randomText();

        headerPage.artistsButtonClick();

        artistPage.clickAddArtistBtn();
        artistPage.setArtistName(artistName);
        artistPage.setArtistPhoto(path);
        artistPage.setArtistBiography(bio);
        artistPage.clickConfirmAddingBtn();

        artistPage.findArtist(artistName);
        artistPage.startSearch();
        artistPage.openArtist(artistName);
        artistPage.clickEditBtn();

        artistPage.editPhoto(editPath);
        artistPage.setArtistName(editedArtistName);
        artistPage.setArtistBiography(editBio);
        artistPage.saveInfo();
        // Твой код
    }
}
