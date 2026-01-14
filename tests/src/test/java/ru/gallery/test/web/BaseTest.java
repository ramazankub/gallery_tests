package ru.gallery.test.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

public class BaseTest {

    private static final String CHROME = "chrome";

    // Перед каждым тестом задаем конфигурации для браузера
    @BeforeEach
    public void setUp() {
        Configuration.timeout = 30000; // Глобальный таймаут для неявных ожиданий
        Configuration.pageLoadStrategy = "eager";
        Configuration.browser = CHROME;
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--no-sandbox");
    }

    // После каждого теста закрываем браузер
    @AfterEach
    public void tearDown() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.closeWebDriver();
        }
    }
}
