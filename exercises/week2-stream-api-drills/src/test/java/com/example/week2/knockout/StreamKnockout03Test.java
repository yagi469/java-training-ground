package com.example.week2.knockout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class StreamKnockout03Test {

    private StreamKnockout03 knockout;
    private List<Employee> employees;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        knockout = new StreamKnockout03();

        employees = List.of(
                new Employee(1L, "Alice", "Engineering", 28, 55000.0),
                new Employee(2L, "Bob", "Sales", 35, 48000.0),
                new Employee(3L, "Charlie", "Engineering", 42, 75000.0),
                new Employee(4L, "Dave", "HR", 29, 42000.0));

        orders = List.of(
                new Order(1L, 1L, "Laptop", 2, 1000.0, LocalDate.of(2024, 1, 15)),
                new Order(2L, 1L, "Mouse", 5, 50.0, LocalDate.of(2024, 1, 20)),
                new Order(3L, 2L, "Keyboard", 3, 100.0, LocalDate.of(2024, 2, 10)),
                new Order(4L, 3L, "Monitor", 1, 500.0, LocalDate.of(2024, 2, 15)),
                new Order(5L, 2L, "Laptop", 1, 1000.0, LocalDate.of(2024, 3, 5)),
                new Order(6L, 4L, "Mouse", 10, 50.0, LocalDate.of(2024, 3, 20)),
                new Order(7L, 1L, "Monitor", 2, 500.0, LocalDate.of(2024, 4, 1)));
    }

    // Level 7: 実務レベル

    @Test
    void problem21_calculateTotalOrderAmount() {
        double total = knockout.calculateTotalOrderAmount(orders);
        assertEquals(5550.0, total, 0.01); // (2*1000)+(5*50)+(3*100)+(1*500)+(1*1000)+(10*50)+(2*500)
    }

    @Test
    void problem22_totalOrderAmountByEmployee() {
        Map<Long, Double> result = knockout.totalOrderAmountByEmployee(orders);
        assertEquals(4, result.size());
        assertEquals(3250.0, result.get(1L), 0.01); // Alice: 2000+250+1000
        assertEquals(1300.0, result.get(2L), 0.01); // Bob: 300+1000
        assertEquals(500.0, result.get(3L), 0.01); // Charlie: 500
        assertEquals(500.0, result.get(4L), 0.01); // Dave: 500
    }

    @Test
    void problem23_findOrdersBetweenDates() {
        LocalDate start = LocalDate.of(2024, 2, 1);
        LocalDate end = LocalDate.of(2024, 3, 31);
        List<Order> result = knockout.findOrdersBetweenDates(orders, start, end);
        assertEquals(4, result.size());
        assertTrue(result.stream().allMatch(o -> !o.getOrderDate().isBefore(start) && !o.getOrderDate().isAfter(end)));
    }

    @Test
    void problem24_totalQuantityByProduct() {
        Map<String, Integer> result = knockout.totalQuantityByProduct(orders);
        assertEquals(4, result.size());
        assertEquals(3, result.get("Laptop"));
        assertEquals(15, result.get("Mouse"));
        assertEquals(3, result.get("Keyboard"));
        assertEquals(3, result.get("Monitor"));
    }

    // Level 8: エキスパート

    @Test
    void problem25_findTopSalesEmployee() {
        Optional<Employee> result = knockout.findTopSalesEmployee(employees, orders);
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName()); // 3250.0
    }

    @Test
    void problem26_top3ProductsByDepartment() {
        Map<String, List<String>> result = knockout.top3ProductsByDepartment(employees, orders);
        assertTrue(result.containsKey("Engineering"));
        assertTrue(result.containsKey("Sales"));

        // Engineering: Alice(Laptop:2000, Monitor:1000, Mouse:250) +
        // Charlie(Monitor:500)
        List<String> engProducts = result.get("Engineering");
        assertTrue(engProducts.size() <= 3);
        assertTrue(engProducts.contains("Laptop"));
    }

    @Test
    void problem27_monthlySales() {
        Map<String, Double> result = knockout.monthlySales(orders);
        assertEquals(4, result.size());
        assertEquals(2250.0, result.get("2024-01"), 0.01);
        assertEquals(800.0, result.get("2024-02"), 0.01);
        assertEquals(1500.0, result.get("2024-03"), 0.01);
        assertEquals(1000.0, result.get("2024-04"), 0.01);
    }

    // Level 9: マスター

    @Test
    void problem28_findAboveAverageSalesEmployees() {
        // Average: (3250+1300+500+500)/4 = 1387.5
        List<Employee> result = knockout.findAboveAverageSalesEmployees(employees, orders);
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("Alice")));
    }

    @Test
    void problem29_findBusiestOrderDate() {
        Optional<LocalDate> result = knockout.findBusiestOrderDate(orders);
        assertTrue(result.isPresent());
        // All dates have 1 order each, so any of them is valid
        assertNotNull(result.get());
    }

    @Test
    void problem30_createSalesRanking() {
        List<Employee> result = knockout.createSalesRanking(employees, orders);
        assertEquals(4, result.size());
        assertEquals("Alice", result.get(0).getName()); // 3250.0
        assertEquals("Bob", result.get(1).getName()); // 1300.0
        // Charlie and Dave both have 500.0, order might vary
    }
}
