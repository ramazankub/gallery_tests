package ru.gallery.test.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import ru.gallery.config.WithAuth;
import ru.gallery.service.AuthApiClient;
import ru.gallery.utils.DataUtils;

public class BaseTest {

    private final AuthApiClient authApiClient = new AuthApiClient();
    protected String bearerToken;

    @BeforeEach
    void createUserAndLogin(TestInfo testInfo) {

        boolean needAuth = testInfo.getTestMethod()
                .map(method -> method.isAnnotationPresent(WithAuth.class))
                .orElse(false);

        if (needAuth) {
            String username = DataUtils.randomUsername();
            authApiClient.createUser(username, DataUtils.DEFAULT_PASSWORD);
            bearerToken = authApiClient.login(username, DataUtils.DEFAULT_PASSWORD);
        }
    }
}