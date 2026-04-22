package ru.gallery.test.api.museum;

import io.qameta.allure.Description;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;
import ru.gallery.config.WithAuth;
import ru.gallery.data.MuseumRepository;
import ru.gallery.data.entity.MuseumEntity;
import ru.gallery.model.ArtistJson;
import ru.gallery.model.MuseumJsonCreateResponse;
import ru.gallery.model.PageResponse;
import ru.gallery.service.MuseumGatewayClient;
import ru.gallery.test.api.BaseTest;
import ru.gallery.utils.StringUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetAllMuseumTest extends BaseTest {
    private final MuseumGatewayClient museumGatewayClient = new MuseumGatewayClient();
    private final MuseumRepository museumRepository = new MuseumRepository();

    @Test
    @WithAuth
    @Description("Проверка что возвращается список музеев")
    void getAllMuseumTest() {
        final int defaultMuseumCount = 10;

        List<MuseumJsonCreateResponse> museumsList = museumGatewayClient.getMuseums(bearerToken)
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<PageResponse<MuseumJsonCreateResponse>>() {})
                .content();

        List<MuseumEntity> museumEntityList = museumRepository.getMuseums(defaultMuseumCount);

        for(MuseumJsonCreateResponse response : museumsList) {
            MuseumEntity museumEntity = museumEntityList.stream()
                    .filter(museum -> museum.getId().equals(response.getId()))
                    .findFirst()
                    .orElseThrow(()-> new AssertionError("Художник c id " + response.getId() +
                            " отсутствует в базе"));

            assertAll("Проверка полей музея",
                    () -> assertEquals(response.getId(), museumEntity.getId()),
                    () -> assertEquals(response.getDescription(), museumEntity.getDescription()),
                    () -> assertEquals(response.getPhoto(), StringUtils.fromUtf8(museumEntity.getPhoto())),
                    () -> assertEquals(response.getTitle(), museumEntity.getTitle()),
                    () -> assertEquals(response.getGeoJson().getCity(), museumEntity.getCity()));
        }
    }
}