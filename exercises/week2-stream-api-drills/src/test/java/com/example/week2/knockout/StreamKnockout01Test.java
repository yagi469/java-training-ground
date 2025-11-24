package com.example.week2.knockout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class StreamKnockout01Test {

    private StreamKnockout01 knockout;
    private List<Employee> employees;

    @BeforeEach
    void setUp() {
        knockout = new StreamKnockout01();
        employees = List.of(
                new Employee(1L, "Alice", "Engineering", 28, 55000.0),
                new Employee(2L, "Bob", "Sales", 35, 48000.0),
                new Employee(3L, "Charlie", "Engineering", 42, 75000.0),
                new Employee(4L, "Dave", "HR", 29, 42000.0),
                new Employee(5L, "Eve", "Engineering", 31, 62000.0),
                new Employee(6L, "Frank", "Sales", 38, 51000.0),
                new Employee(7L, "Grace", "HR", 45, 58000.0));
    }

    // Level 1: 基礎

    @Test
    void problem1_findEmployeesOver30() {
        List<Employee> result = knockout.findEmployeesOver30(employees);
        assertEquals(5, result.size());
        assertTrue(result.stream().allMatch(e -> e.getAge() >= 30));
    }

    @Test
    void problem2_getAllNames() {
        List<String> result = knockout.getAllNames(employees);
        assertEquals(7, result.size());
        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("Grace"));
    }

    @Test
    void problem3_countEngineeringEmployees() {
        long count = knockout.countEngineeringEmployees(employees);
        assertEquals(3, count);
    }

    // Level 2: 中級

    @Test
    void problem4_groupByDepartment() {
        Map<String, List<Employee>> result = knockout.groupByDepartment(employees);
        assertEquals(3, result.size());
        assertEquals(3, result.get("Engineering").size());
        assertEquals(2, result.get("Sales").size());
        assertEquals(2, result.get("HR").size());
    }

    @Test
    void problem5_joinHighEarnerNames() {
        String result = knockout.joinHighEarnerNames(employees);
        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("Charlie"));
        assertTrue(result.contains("Eve"));
        assertTrue(result.contains("Frank"));
        assertTrue(result.contains("Grace"));
        assertFalse(result.contains("Bob"));
        assertFalse(result.contains("Dave"));
    }

    @Test
    void problem6_averageSalaryByDepartment() {
        Map<String, Double> result = knockout.averageSalaryByDepartment(employees);
        assertEquals(3, result.size());
        assertEquals(64000.0, result.get("Engineering"), 0.01);
        assertEquals(49500.0, result.get("Sales"), 0.01);
        assertEquals(50000.0, result.get("HR"), 0.01);
    }

    @Test
    void problem7_findHighestPaidEmployee() {
        Optional<Employee> result = knockout.findHighestPaidEmployee(employees);
        assertTrue(result.isPresent());
        assertEquals("Charlie", result.get().getName());
        assertEquals(75000.0, result.get().getSalary());
    }

    // Level 3: 応用

    @Test
    void problem8_findTopEarnerPerDepartment() {
        Map<String, Optional<Employee>> result = knockout.findTopEarnerPerDepartment(employees);
        assertEquals(3, result.size());

        assertTrue(result.get("Engineering").isPresent());
        assertEquals("Charlie", result.get("Engineering").get().getName());

        assertTrue(result.get("Sales").isPresent());
        assertEquals("Frank", result.get("Sales").get().getName());

        assertTrue(result.get("HR").isPresent());
        assertEquals("Grace", result.get("HR").get().getName());
    }

    @Test
    void problem9_findTop3HighestPaid() {
        List<Employee> result = knockout.findTop3HighestPaid(employees);
        assertEquals(3, result.size());
        assertEquals("Charlie", result.get(0).getName());
        assertEquals("Eve", result.get(1).getName());
        assertEquals("Grace", result.get(2).getName());
    }

    @Test
    void problem10_findDepartmentsWithHighTotalSalary() {
        List<String> result = knockout.findDepartmentsWithHighTotalSalary(employees);
        assertEquals(2, result.size());
        assertTrue(result.contains("Engineering")); // 192000
        assertTrue(result.contains("HR")); // 100000
        assertFalse(result.contains("Sales")); // 99000
    }
}
