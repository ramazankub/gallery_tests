package ru.gallery.test.api.museum;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.gallery.config.WithAuth;
import ru.gallery.data.MuseumRepository;
import ru.gallery.data.entity.MuseumEntity;
import ru.gallery.model.*;
import ru.gallery.service.MuseumGatewayClient;
import ru.gallery.test.api.BaseTest;
import ru.gallery.test.api.geo.countryDataHelpers.DataHelper;
import ru.gallery.test.api.museum.builder.RequestBuilder;
import ru.gallery.utils.StringUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

public class AddMuseumTest extends BaseTest {

    private final MuseumGatewayClient client = new MuseumGatewayClient();
    private final MuseumRepository repository = new MuseumRepository();
    private final RequestBuilder requestBuilder = new RequestBuilder();
    private final DataHelper dataHelper = new DataHelper();

    @Test
    @WithAuth
    @Description("Проверка корректного добавления музея")
    void addMuseumTest() {
        String imagePath = "img/museums/hermitage.jpg";

        AddMuseumRequestJson request = requestBuilder.buildRequest(dataHelper.getValidCountryId(), imagePath);

        Response response = client.addMuseum(request, bearerToken);

        MuseumJsonCreateResponse responseBody = response.then()
                .statusCode(200)
                .extract()
                .as(MuseumJsonCreateResponse.class);


        assertAll("Проверка ответа API",
                () -> assertEquals(request.getTitle(), responseBody.getTitle()),
                () -> assertEquals(request.getDescription(), responseBody.getDescription()),
                () -> assertNotNull(responseBody.getId()),
                () -> assertEquals(request.getGeoJson().getCity(), responseBody.getGeoJson().getCity()),
                () -> assertEquals(
                        request.getGeoJson().getCountryRequestJson().getId(),
                        responseBody.getGeoJson().getCountryRequestJson().getId()
                ),
                () -> assertEquals(request.getPhoto(), responseBody.getPhoto())
        );


        MuseumEntity museum = repository.getMuseumById(responseBody.getId());

        assertAll("Проверка данных в БД",
                () ->assertEquals(responseBody.getId(), museum.getId()),
                () -> assertEquals(responseBody.getTitle(), museum.getTitle()),
                () -> assertEquals(responseBody.getDescription(), museum.getDescription()),
                () -> assertEquals(responseBody.getGeoJson().getCity(), museum.getCity()),
                () -> assertEquals(responseBody.getPhoto(), StringUtils.fromUtf8(museum.getPhoto()))
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
        String imagePath = "img/museums/hermitage.jpg";

        AddMuseumRequestJson request = requestBuilder.buildRequest(dataHelper.getValidCountryId(), imagePath);

        Response response = client.addMuseum(request, token);
        response.then()
                .statusCode(401);

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
        String imagePath = "img/museums/hermitage.jpg";

        AddMuseumRequestJson request = requestBuilder.buildRequest(inValidCountryId,  imagePath);

        Response response = client.addMuseum(request, bearerToken);

        response
                .then()
                .statusCode(400)
                .body("status", equalTo(400));
    }
}
