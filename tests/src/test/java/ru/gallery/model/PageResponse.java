package ru.gallery.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record PageResponse<T>(

        @JsonProperty("totalElements")
        int totalElements,

        @JsonProperty("totalPages")
        int totalPages,

        @JsonProperty("size")
        int size,

        @JsonProperty("content")
        List<T> content
) {
}
