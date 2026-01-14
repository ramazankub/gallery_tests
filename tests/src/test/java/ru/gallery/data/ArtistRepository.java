package ru.gallery.data;

import io.qameta.allure.Step;
import jakarta.persistence.EntityManager;
import ru.gallery.config.Config;
import ru.gallery.data.entity.ArtistEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

import static ru.gallery.data.EntityManagers.em;

// Класс, в котором мы обращаемся к базе данных rococo-artist
public class ArtistRepository {

    private static final Config CFG = Config.getInstance();

    // EntityManager - это штука, которая ходит в БД. Не заморачивайся как, главное передай правильный путь к базе в em()
    private final EntityManager entityManager = em(CFG.artistJdbcUrl());

    @Nonnull
    // Аннотация для описания метода. Нужен для того, чтобы была красивая запись в Allure-отчете
    @Step("Получить художника из базы по с id = {id}")
    public ArtistEntity findArtistById(UUID id) {
        // Здесь описываем запрос. Таблица у нас artist, а id - параметр, который мы передаем в метод
        String sql = "SELECT * FROM artist WHERE id = :id";

        return (ArtistEntity) entityManager
                .createNativeQuery(sql, ArtistEntity.class)
                .setParameter("id", id)
                .getSingleResult(); // так нужно писать, когда возвращаем только одну уникальную запись
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Step("Получить {count} художников из базы")
    public List<ArtistEntity> findAllArtists(int count) {
        String sql = "SELECT * FROM artist";

        return entityManager
                .createNativeQuery(sql, ArtistEntity.class)
                .setMaxResults(count)
                .getResultList(); // так нужно писать, когда нужно несколько записей
    }
}
