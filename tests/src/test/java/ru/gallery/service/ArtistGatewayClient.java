package ru.gallery.service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.gallery.model.ArtistJson;

public class ArtistGatewayClient extends RestAssuredBaseClient {

    private final String path = "/api/artist";

    @Step("Отправка запроса на получение художника")
    public Response getArtist(String id) {
        return spec.when()
                   .get(path + "/{id}", id);
    }

    @Step("Отправка запроса на получение всех художников")
    public Response getAllArtists() {
        return spec.when()
                   .get(path);
    }

    @Step("Отправка запроса на добавление художника")
    public Response addArtist(String token, ArtistJson artist) {
        return spec.header(AUTH_HEADER, token)
                   .body(artist)
                   .when()
                   .post(path);

    }

    @Step("Отправка запроса на обновление художника")
    public Response updateArtist(String token, ArtistJson artist) {
        return spec.header(AUTH_HEADER, token)
                   .body(artist)
                   .when()
                   .patch(path);
    }
}
