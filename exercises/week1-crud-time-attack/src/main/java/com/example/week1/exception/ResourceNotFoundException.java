package com.example.week1.exception;

// Drill 9 - Custom Exception Class (Target: 2 mins)
// Requirements:
// 1. Extend RuntimeException
// 2. Add constructor that takes a message (String)
// 3. This will be thrown when a resource is not found (404)

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
