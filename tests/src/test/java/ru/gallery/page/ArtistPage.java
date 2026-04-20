package ru.gallery.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;


import static com.codeborne.selenide.Selenide.$x;

public class ArtistPage extends BasePage<ArtistPage> {
    private final SelenideElement
            addArtistBtn = $x("//button[contains(text(),'Добавить художника')]"),
            addArtistModalWindow = $x("//*[contains(text(), 'Заполните форму, чтобы добавить нового художника')]"),
            artistNameInput = $x("//*[@class='input' and @placeholder='Введите имя художника...']"),
            setPhotoInput = $x("//*[@class='input' and @placeholder='Изображение художника']"),
            artistBiographyInput = $x("//*[@placeholder='Биография художника']"),
            closeModalBtn = $x("//*[@type='button' and contains(text(),'Закрыть')]"),
            confirmAddingBtn = $x("//*[@type='submit' and contains(text(),'Добавить')]"),
            artistSearchLine = $x("//*[@type='search' and @placeholder='Искать художников...']"),
            searchBtn = $x("//*[@class='btn-icon variant-soft-surface ml-4']"),
            editArtistBtn = $x("//*[@data-testid='edit-artist']"),
            editPhotoInput = $x("//*[@class='input' and @placeholder='Обновить изображение художника']"),
            saveEditions = $x("//*[@type='submit' and contains(text(), 'Сохранить')]");

    @Step("Проверить что кнопка Добавить художника видна")
    public void checkAddArtisBtnIsVisible() {
        checkElementVisible(addArtistBtn);
    }

    @Step("Нажать на кнопку добавить художника")
    public void clickAddArtistBtn() {
        clickByLocator(addArtistBtn);
    }

    @Step("Проверка появления модального окна для добавления художника")
    public void checkAddArtistModalWindowIsVisible() {
        checkElementVisible(addArtistModalWindow);
    }

    @Step("Ввести имя художника")
    public void setArtistName(String value) {
        artistNameInput.shouldBe(Condition.visible);
        artistNameInput.setValue(value);
    }

    @Step("Ввести фото художника")
    public void setArtistPhoto(String value) {
        setPhotoInput.shouldBe(Condition.visible);
        setPhotoInput.uploadFromClasspath(value);
    }

    @Step("Ввести биографию художника")
    public void setArtistBiography(String value) {
        artistBiographyInput.shouldBe(Condition.visible);
        artistBiographyInput.setValue(value);
    }

    @Step("Проверить видимость кнопок модального окна")
    public void checkModalBtnsAreVisible() {
        checkElementVisible(closeModalBtn);
        checkElementVisible(confirmAddingBtn);
    }

    @Step("Подтвердить добавление художника")
    public void clickConfirmAddingBtn() {
        clickByLocator(confirmAddingBtn);
    }

    @Step("Найти художника по имени")
    public SelenideElement findArtist(String name) {
        artistSearchLine.shouldBe(Condition.visible);
        artistSearchLine.setValue(name);
        clickByLocator(searchBtn);

        return $x("//div[contains(@class,'artist')]//*[text()='" + name + "']");
    }

    @Step("Нажать кнопку поиска")
    public void startSearch() {
        clickByLocator(searchBtn);
    }

    @Step("Проверить фото художника")
    public void checkArtistPhoto() {
        $x("//img[contains(@class,'avatar-image')]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.attribute("src"));
    }

    @Step("Открыть художника")
    public void openArtist(String name) {
        $x("//span[contains(normalize-space(), '" + name + "')]")
                .shouldBe(Condition.visible)
                .click();
    }

    @Step("Проверить имя художника")
    public void checkArtistName(String name) {
        $x("//header[contains(@class,'card-header')]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text(name));
    }

    @Step("Проверить биографию художника")
    public void checkArtistBio(String bio) {
        $x("//p[contains(@class,'col-span-2')]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text(bio));
    }

    @Step("Нажать на кнопку Редактировать")
    public void clickEditBtn() {
        clickByLocator(editArtistBtn);
    }

    @Step("Обновить фото")
    public void editPhoto(String path) {
        editPhotoInput.uploadFromClasspath(path);
    }

    @Step("Сохранить обновленную инфу")
    public void saveInfo() {
        clickByLocator(saveEditions);
    }
}
