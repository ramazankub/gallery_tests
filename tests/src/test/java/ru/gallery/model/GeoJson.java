package ru.gallery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoJson {
    @JsonProperty("country")
    private CountryRequestJson countryRequestJson;
    private String city;
}
