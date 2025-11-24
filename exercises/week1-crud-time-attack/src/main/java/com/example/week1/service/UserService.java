package com.example.week1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.week1.dto.UserRegistrationRequest;
import com.example.week1.dto.UserResponse;
import com.example.week1.entity.User;
import com.example.week1.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

// TODO: Drill 8 - Service with Repository (Target: 5 mins)
// Requirements:
// 1. Inject UserRepository (private final UserRepository repository;)
// 2. createUser: 
//    - Create User entity from request
//    - Save to repository
//    - Convert saved entity to UserResponse
// 3. getUser:
//    - Use repository.findById(id)
//    - Use .orElseThrow(() -> new RuntimeException("User not found"))
//    - Convert entity to UserResponse

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;

    @Transactional
    public UserResponse createUser(UserRegistrationRequest request) {
        User user = new User(null, request.username(), request.password());
        User savedUser = repository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getUsername());
    }

    public UserResponse getUser(@NonNull Long id) {
        return repository.findById(id)
                .map(user -> new UserResponse(user.getId(), user.getUsername()))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
