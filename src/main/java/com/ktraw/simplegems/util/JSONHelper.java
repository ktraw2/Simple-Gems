package com.ktraw.simplegems.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JSONHelper {
    public static String getStringOrDefault(String key, JsonObject json, String defaultValue) {
        JsonElement jsonElement = json.get(key);
        return (jsonElement != null) ? ((jsonElement.isJsonNull()) ? defaultValue : jsonElement.getAsString()) : defaultValue;
    }

    public static int getIntOrDefault(String key, JsonObject json, int defaultValue) {
        JsonElement jsonElement = json.get(key);
        return (jsonElement != null) ? ((jsonElement.isJsonNull()) ? defaultValue : jsonElement.getAsInt()) : defaultValue;
    }

    public static boolean getBooleanOrDefault(String key, JsonObject json, boolean defaultValue) {
        JsonElement jsonElement = json.get(key);
        return (jsonElement != null) ? ((jsonElement.isJsonNull()) ? defaultValue : jsonElement.getAsBoolean()) : defaultValue;
    }
}
