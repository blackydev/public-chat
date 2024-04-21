/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models;

public final class UserBuilder {
    private Long id;
    private String email;
    private String username;
    private String password;
    private boolean isAdmin;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    public User build() {
        return new User(id, email, username, password, isAdmin);
    }
}
