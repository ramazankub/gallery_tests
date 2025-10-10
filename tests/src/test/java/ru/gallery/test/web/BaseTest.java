package ru.gallery.test.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.chrome.ChromeOptions;

public class BaseTest {

    private static final String CHROME = "chrome";

    public void setUp() {
        Configuration.timeout = 30000;
        Configuration.pageLoadStrategy = "eager";
        Configuration.browser = CHROME;
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--no-sandbox");
    }

    @AfterEach
    public void tearDown() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.closeWebDriver();
        }
    }
}
