package ru.gallery.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;

// Этот класс описывает body, которое мы используем в запросах
@JsonInclude(JsonInclude.Include.NON_NULL)
// Эта аннотация из библиотеки Lombok позволяет создавать объекты через .builder().build(). Заодно это пример паттерна проектирования Builder
@Builder
public record ArtistJson(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("name")
        String name,

        @JsonProperty("biography")
        String biography,

        @JsonProperty("photo")
        String photo
) {
}
