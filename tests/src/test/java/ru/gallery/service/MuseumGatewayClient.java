package ru.gallery.service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.gallery.model.AddMuseumRequestJson;

public class MuseumGatewayClient extends RestAssuredBaseClient {
    private final String path = "/api/museum";

    @Step("Запрос на добавление музея")
    public Response addMuseum(AddMuseumRequestJson addMuseumRequestJson, String bearerToken) {
        return spec
                .header(AUTH_HEADER, bearerToken)
                .body(addMuseumRequestJson)
                .when()
                .post(path);
    }

    @Step("Запрос на обновление музея")
    public Response updateMuseum(AddMuseumRequestJson addMuseumRequestJson, String bearerToken) {
        return spec
                .header(AUTH_HEADER, bearerToken)
                .body(addMuseumRequestJson)
                .when()
                .patch(path);
    }

    @Step("Запрос на получение музея по ID")
    public Response getMuseumById(String id, String bearerToken) {
        return spec
                .header(AUTH_HEADER, bearerToken)
                .when()
                .get(path + "/{id}", id);
    }

    @Step("Запрос на получение списка всех музеев")
    public Response getMuseums(String bearerToken) {
        return spec
                .header(AUTH_HEADER, bearerToken)
                .when()
                .get(path);
    }
}
