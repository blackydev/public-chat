package com.patryklikus.publicchat.models;

public class User {
    private Long id;
    private final String email;
    private final String username;
    private final String password;
    private final boolean isAdmin;

    User(Long id, String email, String username, String password, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
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

    public void setId(Long id) {
        this.id = id;
    }
}
