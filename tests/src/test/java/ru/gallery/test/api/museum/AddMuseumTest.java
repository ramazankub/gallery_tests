package ru.gallery.test.api.museum;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.gallery.config.WithAuth;
import ru.gallery.data.GeoRepository;
import ru.gallery.data.MuseumRepository;
import ru.gallery.data.entity.MuseumEntity;
import ru.gallery.model.*;
import ru.gallery.service.MuseumGatewayClient;
import ru.gallery.test.api.BaseTest;
import ru.gallery.utils.DataUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

public class AddMuseumTest extends BaseTest {

    private final GeoRepository geoRepository = new GeoRepository();
    private final MuseumGatewayClient client = new MuseumGatewayClient();
    private final MuseumRepository repository = new MuseumRepository();

    AddMuseumRequestJson buildRequest(String countryId) {
        String imagePath = "img/museums/hermitage.jpg";

        return AddMuseumRequestJson.builder()
                .title(DataUtils.randomText())
                .description(DataUtils.randomText())
                .photo(DataUtils.getImageByPathOrEmpty(imagePath))
                .geoJson(
                        GeoJson.builder()
                                .countryRequestJson(CountryRequestJson.builder()
                                        .id(countryId)
                                        .build()
                                )
                                .city(DataUtils.randomText())
                                .build())
                .build();
    }

    String getValidCountryId() {
        return geoRepository.findAllCountries(1)
                .get(0)
                .getId()
                .toString();
    }

    @Test
    @WithAuth
    @Description("Проверка корректного добавления музея")
    void addMuseumTest() {
        Allure.step("Подготовка данных");
        AddMuseumRequestJson request = buildRequest(getValidCountryId());

        Allure.step("Отправка запроса");
        Response response = client.addMuseum(request, bearerToken);

        Allure.step("Проверка ответа");
        MuseumJsonCreateResponse responseBody = response.then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(MuseumJsonCreateResponse.class);

        Allure.step("Проверка через API");

        assertAll("Проверка ответа API",
                () -> assertNotNull(responseBody.getId()),
                () -> assertEquals(request.getTitle(), responseBody.getTitle()),
                () -> assertEquals(request.getDescription(), responseBody.getDescription())
        );

        Allure.step("Проверка в БД");

        MuseumEntity museum = repository.getMuseumById(responseBody.getId());

        assertAll("Проверка данных в БД",
                () -> assertEquals(request.getTitle(), museum.getTitle()),
                () -> assertEquals(request.getDescription(), museum.getDescription()),
                () -> assertEquals(getValidCountryId(), museum.getCountryId().toString()),
                () -> assertEquals(request.getGeoJson().getCity(), museum.getCity())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"",
            "invalid_token",
            "Bearer",
            "Bearer invalid",
            "Bearer 123456"})
    @Description("Проверка ошибки при отсутствии или некорректном токене")
    void addMuseumWithIncorrectToken(String token) {
        Allure.step("Токен из параметра - " + token);
        Allure.step("Базовый токен - " + bearerToken);

        Allure.step("Отправка запроса с невалидным токеном");

        AddMuseumRequestJson request = buildRequest(getValidCountryId());

        Allure.step("Проверка ответа с API");
        Response response = client.addMuseum(request, token);
        response.then()
                .log().all()
                .statusCode(401);
        System.out.println(this.bearerToken + " текущий токен");

        assertAll("Проверка ошибки",
                () -> assertEquals(401, response.statusCode())
        );
    }

    @Test
    @Description("Проверка ошибки при добавлении музея без названия")
    void addMuseumWithoutTitle() {
    }

    @Test
    @Description("Проверка ошибки при добавлении музея без описания")
    void addMuseumWithoutDescription() {
    }

    @Test
    @Description("Проверка ошибки при добавлении музея без фото")
    void addMuseumWithoutPhoto() {
    }

    @Test
    @Description("Проверка ошибки при добавлении музея без местоположения")
    void addMuseumWithoutGeo() {
    }

    @Test
    @WithAuth
    @Description("Проверка ошибки при добавлении музея с несуществующим id страны")
    void addMuseumWithIncorrectCountryId() {
        String inValidCountryId = "!123BadId";

        Allure.step("Подготовка данных");
        AddMuseumRequestJson request = buildRequest(inValidCountryId);

        Allure.step("Отправка запроса");
        Response response = client.addMuseum(request, bearerToken);

        Allure.step("Проверка ответа");
        response
                .then()
                .log().all()
                .statusCode(400)
                .body("status", equalTo(400));
    }
}
