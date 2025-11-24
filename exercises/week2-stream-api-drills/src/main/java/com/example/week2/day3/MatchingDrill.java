package com.example.week2.day3;

import java.util.List;
import java.util.Optional;

public class MatchingDrill {

    public boolean hasUnderage(List<User> users) {
        // Implement using anyMatch
        return users.stream().anyMatch(user -> user.getAge() < 20);
    }

    public boolean areAllInDepartment(List<User> users, String department) {
        // Implement using allMatch
        return users.stream().allMatch(user -> user.getDepartment().equals(department));
    }

    public Optional<User> findYoungestUser(List<User> users) {
        // Implement using min or sorted().findFirst()
        return users.stream().min((user1, user2) -> user1.getAge() - user2.getAge());
    }

    public Optional<User> findFirstUserInDepartment(List<User> users, String department) {
        // Implement using filter and findFirst
        return users.stream().filter(user -> user.getDepartment().equals(department)).findFirst();
    }
}
