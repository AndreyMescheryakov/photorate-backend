package com.vsu.demo.response;

public record ErrorResponse(String message, ErrorCode errorCode) {
}
