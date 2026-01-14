package ru.gallery.config;

import javax.annotation.Nonnull;

/*
Это интерфейс, который обязывает реализовать указанные ниже методы. Он нужен для того, если вдруг решим поднять проект
не через Tuna (пути описаны в TunaConfig), а например локально через Docker. Тогда мы создадим DockerConfig и
опишем новые пути там
 */
public interface Config {

    static @Nonnull Config getInstance() {
        return TunaConfig.INSTANCE;
    }

    @Nonnull
    String jdbcUrl();

    @Nonnull
    String frontUrl();

    @Nonnull
    String authUrl();

    @Nonnull
    String gatewayUrl();

    @Nonnull
    String userdataJdbcUrl();

    @Nonnull
    String artistJdbcUrl();

    @Nonnull
    String geoJdbcUrl();

    @Nonnull
    String museumJdbcUrl();

    @Nonnull
    String paintingJdbcUrl();
}
