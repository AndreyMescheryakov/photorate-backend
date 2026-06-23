package com.vsu.demo.entity;

import java.time.LocalDate;
import java.util.UUID;

public class PhotoFile {
    private UUID id;
    private String fileName;
    private String fileContent;
    private String fileExtension;
    private Integer fileSizeKb;
    private LocalDate uploadDate;

    public PhotoFile(UUID id, String fileName, String fileContent, String fileExtension,
                     Integer fileSizeKb, LocalDate uploadDate) {
        this.id = id;
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.fileExtension = fileExtension;
        this.fileSizeKb = fileSizeKb;
        this.uploadDate = uploadDate;
    }

    public UUID getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public Integer getFileSizeKb() {
        return fileSizeKb;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }
}
