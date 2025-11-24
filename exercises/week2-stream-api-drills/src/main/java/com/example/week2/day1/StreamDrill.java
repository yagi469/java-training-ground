package com.example.week2.day1;

import java.util.List;
import java.util.stream.Collectors;

public class StreamDrill {

    public List<User> filterAdults(List<User> users) {
        return users.stream()
                .filter(user -> user.getAge() >= 20)
                .collect(Collectors.toList());
    }

    public List<String> mapToNames(List<User> users) {
        return users.stream()
                .map(User::getName)
                .collect(Collectors.toList());
    }

    public List<String> getEngineeringUserNamesInUpperCase(List<User> users) {
        return users.stream()
                .filter(user -> "Engineering".equals(user.getDepartment()))
                .map(User::getName)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }
}
