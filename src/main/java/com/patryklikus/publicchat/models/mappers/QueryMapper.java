/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models.mappers;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class QueryMapper {
    public Map<String, String> queryToMap(String query) {
        Map<String, String> formData = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String key = URLDecoder.decode(keyValue[0], UTF_8);
            String value = URLDecoder.decode(keyValue[1], UTF_8);
            formData.put(key, value);
        }
        return formData;
    }
}
