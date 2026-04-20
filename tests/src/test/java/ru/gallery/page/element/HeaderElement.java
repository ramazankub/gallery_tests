package ru.gallery.page.element;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.gallery.page.BasePage;
import ru.gallery.page.LoginPage;
import ru.gallery.page.MainPage;
import ru.gallery.utils.DataUtils;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

// Выделяем в отдельный элемент, т.к. он присутствует почти на всех страницах
public class HeaderElement extends BasePage<HeaderElement> {

    private static final SelenideElement mainButton = $x("//h1");

    private static final SelenideElement paintingsButton = $x("//a[contains(text(), 'Картины')]");

    private static final SelenideElement artistsButton = $x("//a[contains(text(), 'Художники')]");

    private static final SelenideElement museumsButton = $x("//a[contains(text(), 'Музеи')]");

    private static final SelenideElement themeRadioButton = $x("//div[contains(@class, 'lightswitch-track')]");

    private static final SelenideElement enterButton = $x("//button[contains(text(), 'Войти')]");

    private static final SelenideElement avatarButton = $x("//figure[@data-testid='avatar']");

    @Step("Проверка элементов если пользователь авторизован")
    public void checkElementsWithAuthorization() {
        checkElements();
        checkAvatarButton();
    }

    @Step("Проверка элементов если пользователь не авторизован")
    public void checkElementsWithoutAuthorization() {
        checkElements();
        checkEnterButton();
    }

    private void checkElements() {
        mainButton.shouldBe(visible);
        paintingsButton.shouldBe(visible);
        artistsButton.shouldBe(visible);
        museumsButton.shouldBe(visible);
        themeRadioButton.shouldBe(visible);
    }

    @Step("Проверка что кнопка аватара отображается")
    public void checkAvatarButton() {
        avatarButton.shouldBe(visible);
    }

    @Nonnull
    @Step("Проверка аватара в хедере")
    public HeaderElement checkAvatarHeader(String imgPath) {
        SelenideElement actualAvatar = avatarButton.$x("./img");
        String expectedAvatar = DataUtils.getImageByPathOrEmpty(imgPath);

        actualAvatar.shouldHave(attribute("src", expectedAvatar));

        return this;
    }

    @Step("Проверка что кнопка входа отображается")
    public void checkEnterButton() {
        enterButton.shouldBe(visible);
    }

    @Nonnull
    @Step("Нажать на кнопку 'Войти'")
    public LoginPage clickEnterButton() {
        enterButton.click();
        return new LoginPage();
    }

    @Step("Клик по кнопке художники в хедере")
    public void artistsButtonClick() {
        clickByLocator(artistsButton);
    }

    @Nonnull
    @Step("Нажать на кнопку 'Главная страница'")
    public MainPage clickMainButton() {
        mainButton.click();
        return new MainPage();
    }

}
