package com.vsu.demo.entity;

import java.time.LocalDate;
import java.util.UUID;

public class Rating {
    private UUID id;
    private Integer visualAppeal;
    private Integer photoQuality;
    private Integer style;
    private LocalDate createdAt;
    private UUID userId;
    private UUID photoId;

    public Rating(UUID id, Integer visualAppeal, Integer photoQuality, Integer style,
                  LocalDate createdAt, UUID userId, UUID photoId) {
        this.id = id;
        this.visualAppeal = visualAppeal;
        this.photoQuality = photoQuality;
        this.style = style;
        this.createdAt = createdAt;
        this.userId = userId;
        this.photoId = photoId;
    }

    public UUID getId() {
        return id;
    }

    public Integer getVisualAppeal() {
        return visualAppeal;
    }

    public Integer getPhotoQuality() {
        return photoQuality;
    }

    public Integer getStyle() {
        return style;
    }

    public double getOverall() {
        return (visualAppeal + photoQuality + style) / 3.0;
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
