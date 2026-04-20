package ru.gallery.test.api.museum;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import ru.gallery.config.WithAuth;
import ru.gallery.data.MuseumRepository;
import ru.gallery.service.MuseumGatewayClient;
import ru.gallery.test.api.BaseTest;

import java.util.List;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.isA;

public class GetAllMuseumTest extends BaseTest {
    private final MuseumRepository museumRepository = new MuseumRepository();
    private final MuseumGatewayClient museumGatewayClient = new MuseumGatewayClient();

    @Test
    @WithAuth
    @Description("Проверка что возвращается список музеев")
    void getAllMuseumTest() {
        Allure.step("Проверка что в ответ вернулся список");
        museumGatewayClient.getMuseums(bearerToken)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("content", isA(List.class))
                .body("content.size()", greaterThanOrEqualTo(0));
    }
}