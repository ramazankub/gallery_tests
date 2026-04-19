package ru.gallery.test.api.museum;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import ru.gallery.config.WithAuth;
import ru.gallery.data.MuseumRepository;
import ru.gallery.service.MuseumGatewayClient;
import ru.gallery.test.api.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetMuseumTest extends BaseTest {
    private final AddMuseumTest addMuseumTest = new AddMuseumTest();
    private final MuseumGatewayClient museumGatewayClient = new MuseumGatewayClient();
    private final MuseumRepository museumRepository = new MuseumRepository();

    @Test
    @WithAuth
    @Description("Проверка количества музеев (API vs DB)")
    void getMuseumsCountTest() {

        Allure.step("Получим ответ от API");
        Response response = museumGatewayClient.getMuseums(bearerToken)
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        Allure.step("Достанем totalElements из API");
        int apiCount = response.jsonPath().getInt("totalElements");

        Allure.step("Достанем count из БД");
        long dbCount = museumRepository.getMuseumsCount();

        Allure.step("Сравниваем");
        assertEquals(dbCount, apiCount, "Количество музеев не совпадает");
    }
}
