/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models.mappers;

import com.patryklikus.publicchat.models.User;

import java.util.Map;

import static com.patryklikus.publicchat.models.UserBuilder.anUser;

public class UserMapper {
    private final JsonMapper jsonMapper;

    public UserMapper(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public User toUser(String json) {
        Map<String, String> map = jsonMapper.jsonToMap(json);
        if (map.size() < 2) {
            return null;
        }
        String username = map.get("username");
        String password = map.get("password");
        if(username == null || password == null) {
            return null;
        }
        return anUser().withUsername(username)
                .withPassword(password)
                .withIsAdmin(false)
                .build();
    }
}
