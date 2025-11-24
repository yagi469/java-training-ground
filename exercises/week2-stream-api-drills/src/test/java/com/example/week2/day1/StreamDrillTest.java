package com.example.week2.day1;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StreamDrillTest {

    private final StreamDrill drill = new StreamDrill();

    private final List<User> users = List.of(
            new User(1L, "Alice", 25, "Engineering"),
            new User(2L, "Bob", 18, "Sales"),
            new User(3L, "Charlie", 30, "Engineering"),
            new User(4L, "Dave", 20, "HR"));

    @Test
    void filterAdults() {
        List<User> adults = drill.filterAdults(users);
        assertEquals(3, adults.size());
        assertTrue(adults.stream().allMatch(u -> u.getAge() >= 20));
        assertTrue(adults.stream().noneMatch(u -> u.getName().equals("Bob")));
    }

    @Test
    void mapToNames() {
        List<String> names = drill.mapToNames(users);
        assertEquals(4, names.size());
        assertEquals(List.of("Alice", "Bob", "Charlie", "Dave"), names);
    }

    @Test
    void getEngineeringUserNamesInUpperCase() {
        List<String> names = drill.getEngineeringUserNamesInUpperCase(users);
        assertEquals(2, names.size());
        assertEquals(List.of("ALICE", "CHARLIE"), names);
    }
}
