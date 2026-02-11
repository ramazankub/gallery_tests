package ru.gallery.utils;

import java.nio.charset.StandardCharsets;

public class StringUtils {

    public static String fromUtf8(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
