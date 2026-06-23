package com.vsu.demo.request;

public record CreatePhotoRequest(String title, String description, String category,
                                 String fileName, String fileContent, String fileExtension, Integer fileSizeKb) {
}
