package ru.gallery.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import ru.gallery.api.core.ThreadSafeCookieStore;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Objects;

public class ClientUtils {

    public static OkHttpClient getOkHttpClient(boolean followRedirect) {
        return new OkHttpClient.Builder()
                .followRedirects(followRedirect)
                .addNetworkInterceptor(getLoggingInterceptor())
                .addInterceptor(new AllureOkHttp3().setRequestTemplate("request.ftl").setResponseTemplate("response.ftl"))
                .cookieJar(
                        new JavaNetCookieJar(
                                new CookieManager(
                                        ThreadSafeCookieStore.INSTANCE,
                                        CookiePolicy.ACCEPT_ALL
                                )
                        )
                )
                .build();
    }

    private static HttpLoggingInterceptor getLoggingInterceptor() {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                message -> {
                    try {

                        if (isJson(message)) {
                            JsonElement jsonElement = JsonParser.parseString(message);
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            replacePhoto(jsonObject);

                            String prettyJson = gson.toJson(jsonObject);
                            System.out.println(prettyJson);
                        } else {
                            if (!isHtml(message)) {
                                System.out.println(message);
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        System.out.println(message);
                    }
                }
        );
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }

    private static void replacePhoto(JsonObject jsonObject) {
        JsonElement photo = jsonObject.get("photo");
        if (!Objects.isNull(photo)) {
            jsonObject.addProperty("photo", "[truncated from console, because very long]");
        }
    }

    private static boolean isJson(String message) {
        return message.startsWith("{") || message.startsWith("[");
    }

    private static boolean isHtml(String message) {
        return message.contains("<!DOCTYPE");
    }
}
