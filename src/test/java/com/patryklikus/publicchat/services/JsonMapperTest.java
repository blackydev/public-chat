/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.patryklikus.publicchat.exceptions.JsonException;
import com.patryklikus.publicchat.models.mappers.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JsonMapperTest {
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    @DisplayName("Should map JSON to Map<String, String>")
    void jsonToMapTest() throws JsonException {
        String json = """
                {
                "username": "John",
                "password": "Pass1234",
                }
                """;
        Map<String, String> expected = Map.of("username", "John", "password", "Pass1234");

        Map<String, String> actual = jsonMapper.jsonToMap(json);

        assertEquals(expected, actual);
    }
}
