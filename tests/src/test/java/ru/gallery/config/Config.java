package ru.gallery.config;

import javax.annotation.Nonnull;

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
