package com.vsu.demo.entity;

import java.time.LocalDate;
import java.util.UUID;

public class Comment {
    private Integer id;
    private String text;
    private LocalDate createdAt;
    private UUID userId;
    private UUID photoId;
    private String authorNickname;

    public Comment(Integer id, String text, LocalDate createdAt, UUID userId, UUID photoId) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.userId = userId;
        this.photoId = photoId;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getPhotoId() {
        return photoId;
    }
}
