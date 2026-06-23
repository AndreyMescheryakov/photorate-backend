package com.vsu.demo.entity;

import java.time.LocalDate;
import java.util.UUID;

public class Rating {
    private UUID id;
    private Integer score;
    private LocalDate createdAt;
    private UUID userId;
    private UUID photoId;

    public Rating(UUID id, Integer score, LocalDate createdAt, UUID userId, UUID photoId) {
        this.id = id;
        this.score = score;
        this.createdAt = createdAt;
        this.userId = userId;
        this.photoId = photoId;
    }

    public UUID getId() {
        return id;
    }

    public Integer getScore() {
        return score;
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
