/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models.mappers;

import static com.patryklikus.publicchat.models.MessageBuilder.aMessage;
import static com.patryklikus.publicchat.models.UserBuilder.anUser;

import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.models.dtos.GetMessagesRangeDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageMapper {
    private final JsonMapper jsonMapper;
    private final QueryMapper queryMapper;

    public MessageMapper(JsonMapper jsonMapper, QueryMapper queryMapper) {
        this.jsonMapper = jsonMapper;
        this.queryMapper = queryMapper;
    }

    public Message toMessage(long authorId, String json) {
        Map<String, String> map = jsonMapper.jsonToMap(json);
        if (map.size() != 1) {
            return null;
        }
        String content = map.get("content");
        if (content == null) {
            return null;
        }
        return aMessage().withAuthor(anUser().withId(authorId).build())
                .withContent(content)
                .build();
    }

    public GetMessagesRangeDto toMessageRangeDto(String query) {
        Map<String, String> map = queryMapper.queryToMap(query);
        Long minId = getLongMember(map, "minId");
        Long maxId = getLongMember(map, "maxId");
        if (minId == null || maxId == null) {
            return null;
        }
        return new GetMessagesRangeDto(minId, maxId);
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

    private Long getLongMember(Map<String, String> map, String member) {
        try {
            return Long.parseLong(map.get(member));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
