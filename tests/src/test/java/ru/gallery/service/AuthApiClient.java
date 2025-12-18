package ru.gallery.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.gallery.api.AuthApi;
import ru.gallery.api.core.ThreadSafeCookieStore;
import ru.gallery.config.Config;

import javax.annotation.Nonnull;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.gallery.utils.ClientUtils.getOkHttpClient;
import static ru.gallery.utils.OAuthUtils.generateCodeChallenge;
import static ru.gallery.utils.OAuthUtils.generateCodeVerifier;

public class AuthApiClient {

    private static final String RESPONSE_TYPE = "code";

    private static final String SCOPE = "openid";

    private static final Config CFG = Config.getInstance();

    private static final String REDIRECT_URI = CFG.frontUrl() + "authorized";

    private static final String CODE_CHALLENGE_METHOD = "S256";

    private static final String GRANT_TYPE = "authorization_code";

    private static final String CLIENT_ID = "client";

    private final AuthApi authApi;

    public AuthApiClient() {
        OkHttpClient client = getOkHttpClient(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CFG.authUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();
        this.authApi = retrofit.create(AuthApi.class);
    }

    @Step("Создание нового пользователя")
    public void createUser(@Nonnull String username, @Nonnull String password) {
        getRegisterPage();
        registerUser(
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
        );
    }

    @Step("Получение токена пользователя")
    public String login(@Nonnull String username, @Nonnull String password) {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);
        preRequest(codeChallenge);
        String code = oAuthLogin(username, password);

        return "Bearer " + token(code, codeVerifier);
    }

    private void getRegisterPage() {
        final Response<Void> response;
        try {
            response = authApi.getRegisterPage().execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    private void registerUser(String username, String password, String passwordSubmit, String csrf) {
        final Response<Void> response;
        try {
            response = authApi.registerUser(username, password, passwordSubmit, csrf).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
    }

    private void preRequest(String codeChallenge) {
        final Response<Void> response;
        try {
            response = authApi.authorize(
                                      RESPONSE_TYPE,
                                      CLIENT_ID,
                                      SCOPE,
                                      REDIRECT_URI,
                                      codeChallenge,
                                      CODE_CHALLENGE_METHOD)
                              .execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    private String oAuthLogin(String username, String password) {
        final Response<Void> response;
        try {
            response = authApi.login(
                    username,
                    password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());

        String url = response.raw().request().url().toString();
        return StringUtils.substringAfter(url, "code=");
    }

    private String token(String code, String codeVerifier) {
        final Response<JsonNode> response;
        try {
            response = authApi.token(code, REDIRECT_URI, CLIENT_ID, codeVerifier, GRANT_TYPE).execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        assertNotNull(response.body());

        return response.body().path("id_token").asText();
    }
}
