package ru.gallery.test.api.museum;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import ru.gallery.config.WithAuth;
import ru.gallery.data.GeoRepository;
import ru.gallery.data.MuseumRepository;
import ru.gallery.data.entity.MuseumEntity;
import ru.gallery.model.AddMuseumRequestJson;
import ru.gallery.service.MuseumGatewayClient;
import ru.gallery.test.api.BaseTest;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateMuseumTest extends BaseTest {
    private final AddMuseumTest addMuseumTest = new AddMuseumTest();
    private final MuseumGatewayClient client = new MuseumGatewayClient();
    private final MuseumRepository repository = new MuseumRepository();

    @Test
    @WithAuth
    @Description("Проверка корректного обновления музея")
    void updateMuseumTest() {
        Allure.step("Подготовка данных");
        String countryId = addMuseumTest.getValidCountryId();
        String newTitle = "Test";

        Allure.step("Создание музея");
        AddMuseumRequestJson createJson = addMuseumTest.buildRequest(countryId);
        String museumId = client.addMuseum(createJson, bearerToken)
                .then()
                .extract()
                .path("id");

        Allure.step("Обновление музея");
        AddMuseumRequestJson updateJson = addMuseumTest.buildRequest(countryId);
        updateJson.setId(museumId);
        updateJson.setTitle(newTitle);

        Allure.step("Вызов метода обновления");
        client.updateMuseum(updateJson, bearerToken)
                .then()
                .statusCode(200)
                .body("title", equalTo(updateJson.getTitle()))
                .body("description", equalTo(updateJson.getDescription()));

        Allure.step("Проверка сохранения через API");
        client.getMuseumById(museumId, bearerToken)
                .then()
                .statusCode(200)
                .body("id", equalTo(museumId))
                .body("title", equalTo(updateJson.getTitle()));

        Allure.step("Проверка через БД");
        MuseumEntity museum = repository.getMuseumById(UUID.fromString(museumId));
        assertEquals(newTitle, museum.getTitle());
        assertEquals(UUID.fromString(museumId), museum.getId());

    }
}
