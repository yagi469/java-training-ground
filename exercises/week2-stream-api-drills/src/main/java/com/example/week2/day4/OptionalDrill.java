package com.example.week2.day4;

import java.util.Optional;

public class OptionalDrill {

    public String getNameOrDefault(Optional<User> user, String defaultName) {
        return user.map(User::getName).orElse(defaultName);
    }

    public String getUpperCasedNameOrUnknown(Optional<User> user) {
        return user.map(User::getName)
                .map(String::toUpperCase)
                .orElse("UNKNOWN");
    }

    public String getDepartmentOrThrow(Optional<User> user) {
        return user.map(User::getDepartment)
                .orElseThrow(IllegalArgumentException::new);
    }

    public void printNameIfPresent(Optional<User> user) {
        user.map(User::getName).ifPresent(System.out::println);
    }
}
