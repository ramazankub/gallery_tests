package ru.gallery.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "museum")
public class MuseumEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "title")
    private String title;


    @Column(name = "description")
    private String description;

    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "country_id")
    private UUID countryId;

    @Column(name = "city")
    private String city;
}
