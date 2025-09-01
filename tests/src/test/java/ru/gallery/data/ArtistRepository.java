package ru.gallery.data;

import jakarta.persistence.EntityManager;
import ru.gallery.config.Config;
import ru.gallery.data.entity.ArtistEntity;

import javax.annotation.Nonnull;
import java.util.UUID;

import static ru.gallery.data.EntityManagers.em;

public class ArtistRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.artistJdbcUrl());

    @Nonnull
    public ArtistEntity create(ArtistEntity artist) {
        entityManager.joinTransaction();
        entityManager.persist(artist);

        return artist;
    }

    @Nonnull
    public ArtistEntity findArtistById(UUID id) {
        return entityManager.find(ArtistEntity.class, id);
    }
}
