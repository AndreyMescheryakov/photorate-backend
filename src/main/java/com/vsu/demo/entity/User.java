package com.vsu.demo.entity;

import java.time.LocalDate;
import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String nickname;
    private String passwordHash;
    private Role role;
    private LocalDate createdAt;

    public User(UUID id, String email, String nickname, String passwordHash, Role role, LocalDate createdAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
