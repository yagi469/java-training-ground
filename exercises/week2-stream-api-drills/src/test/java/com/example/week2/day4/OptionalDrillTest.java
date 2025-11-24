package com.example.week2.day4;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OptionalDrillTest {

    private final OptionalDrill drill = new OptionalDrill();
    private final User alice = new User(1L, "Alice", 25, "Engineering");

    @Test
    void getNameOrDefault() {
        assertEquals("Alice", drill.getNameOrDefault(Optional.of(alice), "Bob"));
        assertEquals("Bob", drill.getNameOrDefault(Optional.empty(), "Bob"));
    }

    @Test
    void getUpperCasedNameOrUnknown() {
        assertEquals("ALICE", drill.getUpperCasedNameOrUnknown(Optional.of(alice)));
        assertEquals("UNKNOWN", drill.getUpperCasedNameOrUnknown(Optional.empty()));
    }

    @Test
    void getDepartmentOrThrow() {
        assertEquals("Engineering", drill.getDepartmentOrThrow(Optional.of(alice)));
        assertThrows(IllegalArgumentException.class, () -> drill.getDepartmentOrThrow(Optional.empty()));
    }

    @Test
    void printNameIfPresent() {
        // Capture stdout
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        drill.printNameIfPresent(Optional.of(alice));
        assertEquals("Alice" + System.lineSeparator(), outContent.toString());

        outContent.reset();
        drill.printNameIfPresent(Optional.empty());
        assertEquals("", outContent.toString());

        // Reset stdout
        System.setOut(System.out);
    }
}
