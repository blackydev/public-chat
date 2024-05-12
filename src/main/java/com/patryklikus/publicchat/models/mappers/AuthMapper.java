/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models.mappers;

import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.models.User;

import java.util.Map;

import static com.patryklikus.publicchat.models.UserBuilder.anUser;

public class AuthMapper {
    public String toJson(Authentication auth) {
        return String.format("""
                { "userId": %s, "isAdmin": %s }
                """, auth.userId(), auth.isAdmin()
        );
    }
}
