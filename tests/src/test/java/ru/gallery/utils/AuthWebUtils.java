package ru.gallery.utils;

import com.codeborne.selenide.Selenide;
import ru.gallery.config.Config;
import ru.gallery.page.element.HeaderElement;

public class AuthWebUtils {

    private static final Config CFG = Config.getInstance();

    public static void authUser(String username, String password) {
        Selenide.open(CFG.frontUrl(), HeaderElement.class)
                .clickEnterButton()
                .checkElements()
                .setUsername(username)
                .setPassword(password)
                .clickEnterButton()
                .checkAuthorized();
    }
}
