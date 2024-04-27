/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models.mappers;

import static com.patryklikus.publicchat.models.PostBuilder.aMessage;
import static com.patryklikus.publicchat.models.UserBuilder.anUser;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.models.User;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectMapper {
    public User toUser(String form) {
        Map<String, String> map = queryToMap(form);
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

    public Message toMessage(long authorId, String form) {
        Map<String, String> map = queryToMap(form);
        if (map.size() != 1) {
            return null;
        }
        String content = map.get("content");
        return aMessage().withAuthor(anUser().withId(authorId).build())
                .withContent(content)
                .build();
    }


    public String toJson(List<Message> messages) {
        return "[" + messages.stream().map(this::toJson).collect(Collectors.joining(",")) + "]";
    }

    private String toJson(Message message) {
        return String.format("""
                        {
                           "id": %s,
                           "author": {"id": %s, username: "%s"},
                           "content": "%s",
                           "timestamp": "%s"
                        }
                        """,
                message.getId(), message.getAuthor().getId(), message.getAuthor().getUsername(),
                message.getContent(), message.getTimestamp()
        );
    }

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
