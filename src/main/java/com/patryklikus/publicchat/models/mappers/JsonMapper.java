/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models.mappers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JsonMapper {
    /**
     * This method map JSON to Map. It supports only JSON objects with string properties. If json is invalid returns empty map.
     */
    public Map<String, String> jsonToMap(String json) {
        try {
            return jsonToMapUnsafe(json);
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public String jsonStringToString(String jsonString) {
        if (!jsonString.startsWith("\"") || !jsonString.endsWith("\"")) {
            return null;
        }
        return jsonString.substring(1, jsonString.length() - 1);
    }

    private Map<String, String> jsonToMapUnsafe(String json) throws IllegalArgumentException {
        Map<String, String> destination = new HashMap<>();

        String[] keyValuePairs = removeStringWrapper(json, "{", "}")
                .split(",");

        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":");
            if (entry.length != 2) {
                throw new IllegalArgumentException();
            }
            String key = removeStringWrapper(entry[0]);
            String value = removeStringWrapper(entry[1]);
            destination.put(key, value);
        }

        return destination;
    }

    private String removeStringWrapper(String source) throws IllegalArgumentException {
        return removeStringWrapper(source, "\"", "\"");
    }

    /**
     * Checks that string startsWith and endsWith provided argument. If not throws {@link IllegalArgumentException}. If yes, removes it from string.
     */
    private String removeStringWrapper(String source, String startsWith, String endsWith) throws IllegalArgumentException {
        if (!source.startsWith(startsWith) || !source.endsWith(endsWith)) {
            throw new IllegalArgumentException();
        }
        return source.substring(1, source.length() - 1);
    }
}
