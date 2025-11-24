package com.example.week1.entity;

import com.example.week1.validation.ValidEmail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// sDrill 6 - Entity Definition (Target: 3 mins)
// Requirements:
// 1. Class: @Entity, @Table(name = "users")
// 2. Fields:
//    - id: @Id, @GeneratedValue(strategy = GenerationType.IDENTITY)
//    - username: String
//    - password: String
// 3. Use Lombok: @Getter, @NoArgsConstructor, @AllArgsConstructor

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ValidEmail
    @Column(nullable = false, unique = true)
    private String email;
}
