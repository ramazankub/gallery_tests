package ru.gallery.test.api.museum.builder;

import ru.gallery.model.AddMuseumRequestJson;
import ru.gallery.model.CountryRequestJson;
import ru.gallery.model.GeoJson;
import ru.gallery.utils.DataUtils;

import java.util.UUID;

public class RequestBuilder {

    public AddMuseumRequestJson buildRequest(String countryId, String imagePath) {

        return AddMuseumRequestJson.builder()
                .title(DataUtils.randomText())
                .description(DataUtils.randomText())
                .photo(DataUtils.getImageByPathOrEmpty(imagePath))
                .geoJson(
                        GeoJson.builder()
                                .countryRequestJson(CountryRequestJson.builder()
                                        .id(countryId)
                                        .build()
                                )
                                .city(DataUtils.randomText())
                                .build())
                .build();
    }

    public AddMuseumRequestJson buildRequestToUpdate(String title, String countryId, UUID id, String imagePath) {

        return AddMuseumRequestJson.builder()
                .id(String.valueOf(id))
                .title(title)
                .description(DataUtils.randomText())
                .photo(DataUtils.getImageByPathOrEmpty(imagePath))
                .geoJson(
                        GeoJson.builder()
                                .countryRequestJson(CountryRequestJson.builder()
                                        .id(countryId)
                                        .build()
                                )
                                .city(DataUtils.randomText())
                                .build())
                .build();
    }
}
