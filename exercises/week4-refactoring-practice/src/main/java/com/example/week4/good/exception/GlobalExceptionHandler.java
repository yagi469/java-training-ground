package com.example.week4.good.exception;

import com.example.week4.common.dto.ErrorResponse;
import com.example.week4.common.exception.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // BookNotFoundExceptionのハンドリング
  @ExceptionHandler(BookNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException e) {
    // ヒント: ErrorResponse error = new ErrorResponse("NOT_FOUND", e.getMessage());
    ErrorResponse error = new ErrorResponse("NOT_FOUND", e.getMessage());
    // ヒント: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  // バリデーションエラーのハンドリング
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException e) {
    // ヒント: バリデーションエラーのメッセージを取得
    // ヒント: String message = e.getBindingResult().getFieldErrors().stream()
    // .map(error -> error.getField() + ": " + error.getDefaultMessage())
    // .collect(Collectors.joining(", "));
    String message = e.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));
    // ヒント: ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", message);
    ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", message);
    // ヒント: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
}
