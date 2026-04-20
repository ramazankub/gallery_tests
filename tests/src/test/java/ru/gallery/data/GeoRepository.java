package ru.gallery.data;

import io.qameta.allure.Step;
import jakarta.persistence.EntityManager;
import ru.gallery.config.Config;
import ru.gallery.data.entity.CountryEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

import static ru.gallery.data.EntityManagers.em;

public class GeoRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.geoJdbcUrl());

    @Nonnull
    @Step("Получить страну из базы по id = {id}")
    public CountryEntity findCountryById(UUID id) {
        String sql = "SELECT * FROM country WHERE id = :id";

        return (CountryEntity) entityManager
                .createNativeQuery(sql, CountryEntity.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Step("Получить {count} стран из базы")
    public List<CountryEntity> findAllCountries(int count) {
        String sql = "SELECT * FROM country ORDER BY id";

        return entityManager
                .createNativeQuery(sql, CountryEntity.class)
                .setMaxResults(count)
                .getResultList();
    }
}