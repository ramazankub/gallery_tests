package ru.gallery.data;

import io.qameta.allure.Step;
import jakarta.persistence.EntityManager;
import ru.gallery.config.Config;
import ru.gallery.data.entity.ArtistEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

import static ru.gallery.data.EntityManagers.em;

public class ArtistRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.artistJdbcUrl());

    @Nonnull
    @Step("Получить художника из базы по с id = {id}")
    public ArtistEntity findArtistById(UUID id) {
        String sql = "SELECT * FROM artist WHERE id = :id";

        return (ArtistEntity) entityManager
                .createNativeQuery(sql, ArtistEntity.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Step("Получить {count} художников из базы")
    public List<ArtistEntity> findAllArtists(int count) {
        String sql = "SELECT * FROM artist";

        return entityManager
                .createNativeQuery(sql, ArtistEntity.class)
                .setMaxResults(count)
                .getResultList();
    }
}
