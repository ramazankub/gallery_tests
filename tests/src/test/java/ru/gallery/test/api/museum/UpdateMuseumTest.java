package ru.gallery.test.api.museum;

import io.qameta.allure.Description;

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
import ru.gallery.utils.DataUtils;
import ru.gallery.utils.StringUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UpdateMuseumTest extends BaseTest {
    private final MuseumGatewayClient client = new MuseumGatewayClient();
    private final MuseumRepository repository = new MuseumRepository();
    private final RequestBuilder requestBuilder = new RequestBuilder();
    private final DataHelper dataHelper = new DataHelper();

    @Test
    @WithAuth
    @Description("Проверка корректного обновления музея")
    void updateMuseumTest() {

        String newTitle = DataUtils.randomMuseumName();
        String imagePath = "img/museums/hermitage.jpg";

        AddMuseumRequestJson request = requestBuilder.buildRequest(dataHelper.getValidCountryId(), imagePath);

        MuseumJsonCreateResponse response = client.addMuseum(request, bearerToken)
                .then()
                .statusCode(200)
                .extract()
                .as(MuseumJsonCreateResponse.class);

        MuseumJsonCreateResponse museumToUpdate = client.getMuseumById(String.valueOf(response.getId()), bearerToken)
                .then()
                .statusCode(200)
                .extract()
                .as(MuseumJsonCreateResponse.class);

        AddMuseumRequestJson toUpdateRequest = requestBuilder.buildRequestToUpdate(newTitle, dataHelper.getValidCountryId(),
                response.getId(), imagePath);

        MuseumJsonCreateResponse updateResponse = client.updateMuseum(toUpdateRequest, bearerToken)
                .then()
                .statusCode(200)
                .extract()
                .as(MuseumJsonCreateResponse.class);

        assertAll("Сверки запроса и ответа с API",
                () -> assertEquals(toUpdateRequest.getTitle(), updateResponse.getTitle()),
                () -> assertEquals(toUpdateRequest.getPhoto(), updateResponse.getPhoto()),
                () -> assertEquals(toUpdateRequest.getDescription(), updateResponse.getDescription()),
                () -> assertEquals(toUpdateRequest.getId(), updateResponse.getId().toString())
        );

        MuseumEntity actualMuseum = repository.getMuseumById(museumToUpdate.getId());
        assertAll("Сверка с БД",
                () -> assertEquals(updateResponse.getPhoto(), StringUtils.fromUtf8(actualMuseum.getPhoto())),
                () -> assertEquals(updateResponse.getTitle(), actualMuseum.getTitle()),
                () -> assertEquals(updateResponse.getId(), actualMuseum.getId()),
                () -> assertEquals(updateResponse.getDescription(), actualMuseum.getDescription())
        );
    }
}