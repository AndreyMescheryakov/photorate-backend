package com.vsu.demo.exception;

import com.vsu.demo.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(final ValidationException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage(), exception.getErrorCode()));
    }
}
