package com.example.week1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: Drill 6 - Entity Definition (Target: 3 mins)
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
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
}
