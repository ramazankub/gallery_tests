package ru.gallery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MuseumJsonCreateResponse {
    private UUID id;
    private String title;
    private String description;
    private String photo;
    @JsonProperty("geo")
    private GeoJson geoJson;
}
