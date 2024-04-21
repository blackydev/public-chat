/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models;

public class User {
    private final String username;
    private final String password;
    private final boolean isAdmin;
    private Long id;

    User(Long id, String username, String password, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
