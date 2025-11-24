package com.example.week2.day3;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class MatchingDrillTest {

    private final MatchingDrill drill = new MatchingDrill();

    private final List<User> users = List.of(
            new User(1L, "Alice", 25, "Engineering"),
            new User(2L, "Bob", 18, "Sales"),
            new User(3L, "Charlie", 30, "Engineering"),
            new User(4L, "Dave", 20, "HR"));

    @Test
    void hasUnderage() {
        assertTrue(drill.hasUnderage(users), "Should return true because Bob is 18");

        List<User> adults = List.of(
                new User(1L, "Alice", 25, "Engineering"),
                new User(3L, "Charlie", 30, "Engineering"));
        assertFalse(drill.hasUnderage(adults), "Should return false because all are adults");
    }

    @Test
    void areAllInDepartment() {
        assertFalse(drill.areAllInDepartment(users, "Engineering"), "Not all users are in Engineering");

        List<User> engineers = List.of(
                new User(1L, "Alice", 25, "Engineering"),
                new User(3L, "Charlie", 30, "Engineering"));
        assertTrue(drill.areAllInDepartment(engineers, "Engineering"), "All users are in Engineering");
    }

    @Test
    void findYoungestUser() {
        Optional<User> youngest = drill.findYoungestUser(users);
        assertTrue(youngest.isPresent());
        assertEquals("Bob", youngest.get().getName());
        assertEquals(18, youngest.get().getAge());

        Optional<User> empty = drill.findYoungestUser(List.of());
        assertTrue(empty.isEmpty());
    }

    @Test
    void findFirstUserInDepartment() {
        Optional<User> hrUser = drill.findFirstUserInDepartment(users, "HR");
        assertTrue(hrUser.isPresent());
        assertEquals("Dave", hrUser.get().getName());

        Optional<User> marketingUser = drill.findFirstUserInDepartment(users, "Marketing");
        assertTrue(marketingUser.isEmpty());
    }
}
