package ru.gallery.data;

import io.qameta.allure.Step;
import jakarta.persistence.EntityManager;
import ru.gallery.config.Config;
import ru.gallery.data.entity.MuseumEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

import static ru.gallery.data.EntityManagers.em;

public class MuseumRepository {
    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = em(CFG.museumJdbcUrl());

    @Nonnull

    @Step("Запрос на получение музея из БД по его ID")
    public MuseumEntity getMuseumById(UUID id) {
        String sql = "SELECT * FROM museum WHERE id = :id";
        return (MuseumEntity) entityManager
                .createNativeQuery(sql, MuseumEntity.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Step("Запрос на получение всех музеев")
    public List<MuseumEntity> getMuseums() {
        String sql = "SELECT * FROM museum";
        return entityManager
                .createNativeQuery(sql, MuseumEntity.class)
                .getResultList();
    }

    @Step("Запрос на получение количества всех музеев")
    public long getMuseumsCount() {
        String sql = "SELECT COUNT(*) FROM museum";
        return (Long) entityManager
                .createNativeQuery(sql)
                .getSingleResult();
    }
}