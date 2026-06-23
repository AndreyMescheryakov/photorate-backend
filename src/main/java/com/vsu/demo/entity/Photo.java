package com.vsu.demo.entity;

import java.time.LocalDate;
import java.util.UUID;

public class Photo {
    private UUID id;
    private String title;
    private String description;
    private String category;
    private LocalDate createdAt;
    private UUID userId;
    private UUID photoFileId;
    private Integer version;

    public Photo(UUID id, String title, String description, String category, LocalDate createdAt,
                 UUID userId, UUID photoFileId, Integer version) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.createdAt = createdAt;
        this.userId = userId;
        this.photoFileId = photoFileId;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getPhotoFileId() {
        return photoFileId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
