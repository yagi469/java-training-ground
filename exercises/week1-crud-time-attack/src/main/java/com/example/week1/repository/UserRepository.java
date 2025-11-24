package com.example.week1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.week1.entity.User;

// Drill 7 - Repository Definition (Target: 2 mins)
// Requirements:
// 1. Interface: extends JpaRepository<User, Long>
// 2. No @Repository annotation needed (Spring auto-detects)
// 3. Optional: Add custom query method (e.g., Optional<User> findByUsername(String username))

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
