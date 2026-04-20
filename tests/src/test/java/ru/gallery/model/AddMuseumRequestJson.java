package ru.gallery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddMuseumRequestJson {
    private String id;
    private String title;
    private String description;
    private String photo;
    @JsonProperty("geo")
    private GeoJson geoJson;
}
