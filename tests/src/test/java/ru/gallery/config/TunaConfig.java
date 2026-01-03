package ru.gallery.config;

import javax.annotation.Nonnull;

public enum TunaConfig implements Config {

    INSTANCE;

    @Nonnull
    @Override
    public String frontUrl() {
        return "https://gallery.ru.tuna.am/";
    }

    @Nonnull
    @Override
    public String authUrl() {
        return "https://gallery-auth.ru.tuna.am/";
    }

    @Nonnull
    @Override
    public String gatewayUrl() {
        return "https://gateway-service.ru.tuna.am/";
    }

    @Nonnull
    public String jdbcUrl() {
        return "jdbc:postgresql://ru.tuna.am:37038/";
    }

    @Nonnull
    @Override
    public String userdataJdbcUrl() {
        return jdbcUrl() + "rococo-userdata";
    }

    @Nonnull
    @Override
    public String artistJdbcUrl() {
        return jdbcUrl() + "rococo-artist";
    }

    @Nonnull
    @Override
    public String geoJdbcUrl() {
        return jdbcUrl() + "rococo-geo";
    }

    @Nonnull
    @Override
    public String museumJdbcUrl() {
        return jdbcUrl() + "rococo-museum";
    }

    @Nonnull
    @Override
    public String paintingJdbcUrl() {
        return jdbcUrl() + "rococo-painting";
    }
}
