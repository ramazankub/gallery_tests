package ru.gallery.test.api.geo;

import io.qameta.allure.Description;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.gallery.config.WithAuth;
import ru.gallery.data.GeoRepository;
import ru.gallery.data.entity.CountryEntity;
import ru.gallery.model.CountryJson;
import ru.gallery.model.PageResponse;
import ru.gallery.service.GeoGatewayClient;
import ru.gallery.test.api.BaseTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GetAllCountriesTest extends BaseTest {
    private final GeoGatewayClient geoGatewayClient = new GeoGatewayClient();
    private final GeoRepository geoRepository = new GeoRepository();

    @Test
    @WithAuth
    @Description("Проверка корректного получения всех стран")
    void getAllCountriesTest() {
        // Отправляем запрос, идем в базу, сравниваем получившееся между собой
        final int defaultCountryCount = 10;

        List<CountryJson> countryJsonList = geoGatewayClient.getCountries(0, defaultCountryCount, null,
                        bearerToken)
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<PageResponse<CountryJson>>() {
                })
                .content();

        assertAll("Проверка списка стран",
                () -> assertFalse(countryJsonList.isEmpty()),
                () -> assertEquals(defaultCountryCount, countryJsonList.size())
        );

        List<CountryEntity> countryEntityList = geoRepository.findAllCountries(defaultCountryCount);

        assertEquals(countryJsonList.size(), countryEntityList.size());

        for (CountryJson actualCountry : countryJsonList) {
            CountryEntity expectedCountry = countryEntityList.stream()
                    .filter(country -> country.getId().equals(UUID.fromString(actualCountry.id())))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError(
                            "Страна с id " + actualCountry.id() + " отсутствует в базе")
                    );

            assertAll("Проверка полей страны",
                    () -> assertEquals(actualCountry.id(), expectedCountry.getId().toString()),
                    () -> assertEquals(actualCountry.name(), expectedCountry.getName())
            );
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "invalid_token",
            "Bearer",
            "Bearer invalid",
            "Bearer 123456"})
    @Description("Проверка ошибки при отсутствии или некорректном токене")
    @DisplayName("Найти все страны при невалидном авторизационном токене")
    void getAllCountriesWithIncorrectToken(String token) {

        final int defaultCountryCount = 10;

        geoGatewayClient.getCountries(0, defaultCountryCount, null, token)
                .then()
                .statusCode(401);
    }
}
