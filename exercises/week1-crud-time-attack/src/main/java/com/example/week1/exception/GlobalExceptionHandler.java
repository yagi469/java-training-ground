package com.example.week1.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// TODO: Drill 10 - Global Exception Handler (Target: 5 mins)
// Requirements:
// 1. Class: @RestControllerAdvice
// 2. Method: @ExceptionHandler(ResourceNotFoundException.class)
//    - Return ResponseEntity<ErrorResponse>
//    - Status: HttpStatus.NOT_FOUND (404)
// 3. Create ErrorResponse record:
//    - String message
//    - int status
//    - LocalDateTime timestamp

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

record ErrorResponse(String message, int status, LocalDateTime timestamp) {
}
