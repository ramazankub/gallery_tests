package ru.gallery.test.api.museum;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import ru.gallery.config.WithAuth;
import ru.gallery.data.MuseumRepository;
import ru.gallery.data.entity.MuseumEntity;
import ru.gallery.model.AddMuseumRequestJson;
import ru.gallery.model.MuseumJsonCreateResponse;
import ru.gallery.service.MuseumGatewayClient;
import ru.gallery.test.api.BaseTest;
import ru.gallery.test.api.geo.countryDataHelpers.DataHelper;
import ru.gallery.test.api.museum.builder.RequestBuilder;
import ru.gallery.utils.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

public class GetMuseumTest extends BaseTest {
    private final MuseumGatewayClient museumGatewayClient = new MuseumGatewayClient();
    private final MuseumRepository museumRepository = new MuseumRepository();
    private final RequestBuilder requestBuilder = new RequestBuilder();
    private final DataHelper dataHelper = new DataHelper();

    @Test
    @WithAuth
    @Description("Проверка количества музеев (API vs DB)")
    void getMuseumsCountTest() {
        String imagePath = "img/museums/louvre.jpg";

        AddMuseumRequestJson request = requestBuilder.buildRequest(dataHelper.getValidCountryId(), imagePath);

        Response response = museumGatewayClient.addMuseum(request, bearerToken);

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

        MuseumEntity museum = museumRepository.getMuseumById(responseBody.getId());

        assertAll("Проверка данных в БД",
                () ->assertEquals(responseBody.getId(), museum.getId()),
                () -> assertEquals(responseBody.getTitle(), museum.getTitle()),
                () -> assertEquals(responseBody.getDescription(), museum.getDescription()),
                () -> assertEquals(responseBody.getGeoJson().getCity(), museum.getCity()),
                () -> assertEquals(responseBody.getPhoto(), StringUtils.fromUtf8(museum.getPhoto()))
        );
    }
}
