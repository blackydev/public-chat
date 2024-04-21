/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models.mappers;

import static com.patryklikus.publicchat.models.UserBuilder.anUser;

import com.patryklikus.publicchat.models.User;
import java.util.Map;

public class ObjectMapper extends JsonMapper {
    public User toUser(String json) {
        Map<String, String> map = jsonToMap(json);
        if (map.size() != 2) {
            return null;
        }
        String username = map.get("username");
        String password = map.get("password");
        return anUser().withUsername(username)
                .withPassword(password)
                .withIsAdmin(false)
                .build();
    }
}
