package com.example.week2.day2;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class CollectorsDrillTest {

    private final CollectorsDrill drill = new CollectorsDrill();

    private final List<Product> products = List.of(
            new Product(1L, "iPhone", "Electronics", 999.99),
            new Product(2L, "MacBook", "Electronics", 1999.99),
            new Product(3L, "Harry Potter", "Books", 19.99),
            new Product(4L, "Clean Code", "Books", 39.99),
            new Product(5L, "Apple", "Food", 0.99));

    @Test
    void groupByCategory() {
        Map<String, List<Product>> grouped = drill.groupByCategory(products);

        assertNotNull(grouped, "Method should not return null");
        assertEquals(3, grouped.size());
        assertEquals(2, grouped.get("Electronics").size());
        assertEquals(2, grouped.get("Books").size());
        assertEquals(1, grouped.get("Food").size());

        assertTrue(grouped.get("Electronics").stream().anyMatch(p -> p.getName().equals("iPhone")));
    }

    @Test
    void joinProductNames() {
        String joined = drill.joinProductNames(products);

        assertNotNull(joined, "Method should not return null");
        // Expected: "iPhone, MacBook, Harry Potter, Clean Code, Apple"
        assertTrue(joined.contains("iPhone"));
        assertTrue(joined.contains("Apple"));
        assertTrue(joined.contains(", "));
        assertEquals("iPhone, MacBook, Harry Potter, Clean Code, Apple", joined);
    }

    @Test
    void countByCategory() {
        Map<String, Long> counts = drill.countByCategory(products);

        assertNotNull(counts, "Method should not return null");
        assertEquals(3, counts.size());
        assertEquals(2L, counts.get("Electronics"));
        assertEquals(2L, counts.get("Books"));
        assertEquals(1L, counts.get("Food"));
    }
}
