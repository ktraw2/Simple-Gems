package com.ktraw.simplegems.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JSONHelper {
    public static String getStringOrDefault(
            final String key,
            final JsonObject json,
            final String defaultValue
    ) {
        final JsonElement jsonElement = json.get(key);
        return (jsonElement != null) ? ((jsonElement.isJsonNull()) ? defaultValue : jsonElement.getAsString()) : defaultValue;
    }

    public static int getIntOrDefault(
            final String key,
            final JsonObject json,
            final int defaultValue
    ) {
        final JsonElement jsonElement = json.get(key);
        return (jsonElement != null) ? ((jsonElement.isJsonNull()) ? defaultValue : jsonElement.getAsInt()) : defaultValue;
    }

    public static boolean getBooleanOrDefault(
            final String key,
            final JsonObject json,
            final boolean defaultValue
    ) {
        final JsonElement jsonElement = json.get(key);
        return (jsonElement != null) ? ((jsonElement.isJsonNull()) ? defaultValue : jsonElement.getAsBoolean()) : defaultValue;
    }
}
