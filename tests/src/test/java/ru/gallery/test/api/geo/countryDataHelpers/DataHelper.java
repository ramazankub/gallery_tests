package ru.gallery.test.api.geo.countryDataHelpers;

import ru.gallery.data.GeoRepository;

public class DataHelper {
    private final GeoRepository geoRepository =  new GeoRepository();

    public String getValidCountryId() {
        return geoRepository.findAllCountries(1)
                .get(0)
                .getId()
                .toString();
    }
}
