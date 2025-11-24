package com.example.week1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.week1.dto.UserRegistrationRequest;
import com.example.week1.dto.UserResponse;
import com.example.week1.dto.UserUpdateRequest;
import com.example.week1.entity.User;
import com.example.week1.exception.ResourceNotFoundException;
import com.example.week1.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

// Drill 11 - Update Service to use Custom Exception (Target: 3 mins)
// Requirements:
// 1. Import ResourceNotFoundException
// 2. Replace RuntimeException with ResourceNotFoundException in getUser method
// 3. Include the id in the error message: "User not found with id: " + id

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;

    @Transactional
    public UserResponse createUser(UserRegistrationRequest request) {
        User user = new User(null, request.username(), request.password(), request.email());
        User savedUser = repository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public UserResponse getUser(@NonNull Long id) {
        return repository.findById(id)
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public List<UserResponse> getUsers() {
        return repository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public UserResponse updateUser(@NonNull Long id, UserUpdateRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setEmail(request.email());
        User updatedUser = repository.save(user);
        return new UserResponse(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail());
    }

    public void deleteUser(@NonNull Long id) {
        repository.deleteById(id);
    }
}
