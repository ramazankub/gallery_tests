package ru.gallery.service;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.gallery.config.Config;

public class RestAssuredBaseClient {

    protected static final String AUTH_HEADER = "Authorization";

    private static final Config CFG = Config.getInstance();

    protected final RequestSpecification spec;

    protected RestAssuredBaseClient() {
        this.spec = RestAssured
                .given()
                .baseUri(CFG.gatewayUrl())
                .contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
    }
}
