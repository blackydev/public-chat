/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models.mappers;

import com.patryklikus.publicchat.exceptions.JsonException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class JsonMapper {
    /**
     * This method map JSON to Map. It supports only JSON objects with string properties. If json is invalid returns empty map.
     */
    protected Map<String, String> jsonToMap(String json) {
        try {
            return jsonToMapUnsafe(json);
        } catch (JsonException e) {
            return Collections.emptyMap();
        }
    }

    private Map<String, String> jsonToMapUnsafe(String json) throws JsonException {
        Map<String, String> destination = new HashMap<>();
        json = json.replaceAll("\\s+", ""); // removes all whitespaces

        String[] keyValuePairs = removeStringWrapper(json, "{", "}")
                .split(",");

        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":");
            if (entry.length != 2) {
                throw new JsonException();
            }
            String key = removeStringWrapper(entry[0]);
            String value = removeStringWrapper(entry[1]);
            destination.put(key, value);
        }

        return destination;
    }

    private String removeStringWrapper(String source) throws JsonException {
        return removeStringWrapper(source, "\"", "\"");
    }

    /**
     * Checks that string startsWith and endsWith provided argument. If not throws {@link JsonException}. If yes, removes it from string.
     */
    private String removeStringWrapper(String source, String startsWith, String endsWith) throws JsonException {
        if (!source.startsWith(startsWith) || !source.endsWith(endsWith)) {
            throw new JsonException();
        }
        return source.substring(1, source.length() - 1);
    }
}
