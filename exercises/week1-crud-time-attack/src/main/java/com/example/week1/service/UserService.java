package com.example.week1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.week1.dto.UserRegistrationRequest;
import com.example.week1.dto.UserResponse;

import lombok.RequiredArgsConstructor;

// Drill 4 - Service Layer Implementation (Target: 5 mins)
// Requirements:
// 1. Class: @Service, @RequiredArgsConstructor, @Transactional(readOnly = true)
// 2. Method 1: createUser(UserRegistrationRequest request)
//    - @Transactional (write)
//    - Return: UserResponse (dummy)
// 3. Method 2: getUser(Long id)
//    - Return: UserResponse (dummy)
//    - Note: No need to implement Repository yet. Just return dummies.

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    @Transactional
    public UserResponse createUser(UserRegistrationRequest request) {
        return new UserResponse(1L, request.username());
    }

    public UserResponse getUser(Long id) {
        return new UserResponse(id, "dummyUser");
    }
}
