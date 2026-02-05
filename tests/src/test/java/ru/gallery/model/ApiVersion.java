package ru.gallery.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApiVersion {
    V1("v1.0");

    private final String version;

    @Override
    public String toString() {
        return version;
    }
}
