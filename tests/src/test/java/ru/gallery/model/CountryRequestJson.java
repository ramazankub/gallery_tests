package ru.gallery.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryRequestJson {
    private String id;
    private String name;
}
