package ru.gallery.service;

import io.qameta.allure.Step;
import io.restassured.response.Response;

public class GeoGatewayClient extends RestAssuredBaseClient {
    private final String path = "/api/country";

    @Step("Запрос на получение информации о странах")
    public Response getCountries(int page, int size, String sort, String bearerToken) {
        return  spec
                .header(AUTH_HEADER, bearerToken)
                .queryParam("page", page)
                .queryParam("size", size)
                .get(path);
    }
}
