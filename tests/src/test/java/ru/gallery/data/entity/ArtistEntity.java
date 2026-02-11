package ru.gallery.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

// Этот класс описывает таблицу. Поля класса - это столбцы в таблице artist.
// ArtistEntity - один художник, List<ArtistEntity> - список художников
@Entity
@Table(name = "artist")
@Getter
@Setter
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String biography;

    @Column(columnDefinition = "bytea")
    private byte[] photo;
}
