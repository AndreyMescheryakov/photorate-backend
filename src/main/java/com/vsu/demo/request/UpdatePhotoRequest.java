package com.vsu.demo.request;

public record UpdatePhotoRequest(String title, String description, String category, Integer version) {
}
