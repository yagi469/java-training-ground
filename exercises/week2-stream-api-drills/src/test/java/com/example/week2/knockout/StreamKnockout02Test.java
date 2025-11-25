package com.example.week2.knockout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class StreamKnockout02Test {

    private StreamKnockout02 knockout;
    private List<Employee> employees;

    @BeforeEach
    void setUp() {
        knockout = new StreamKnockout02();
        employees = List.of(
                new Employee(1L, "Alice", "Engineering", 28, 55000.0),
                new Employee(2L, "Bob", "Sales", 35, 48000.0),
                new Employee(3L, "Charlie", "Engineering", 42, 75000.0),
                new Employee(4L, "Dave", "HR", 29, 42000.0),
                new Employee(5L, "Eve", "Engineering", 31, 62000.0),
                new Employee(6L, "Frank", "Sales", 38, 51000.0),
                new Employee(7L, "Grace", "HR", 45, 58000.0),
                new Employee(8L, "Henry", "Engineering", 26, 50000.0));
    }

    // Level 4: 実践

    @Test
    void problem11_calculateTotalSalary() {
        double total = knockout.calculateTotalSalary(employees);
        assertEquals(441000.0, total, 0.01);
    }

    @Test
    void problem12_maxSalaryByDepartment() {
        Map<String, Double> result = knockout.maxSalaryByDepartment(employees);
        assertEquals(3, result.size());
        assertEquals(75000.0, result.get("Engineering"));
        assertEquals(51000.0, result.get("Sales"));
        assertEquals(58000.0, result.get("HR"));
    }

    @Test
    void problem13_findEmployeesWithNameContaining() {
        List<Employee> result = knockout.findEmployeesWithNameContaining(employees, "a");
        assertEquals(5, result.size()); // Alice, Charlie, Dave, Frank, Grace
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("Alice")));
        assertFalse(result.stream().anyMatch(e -> e.getName().equals("Bob")));
    }

    @Test
    void problem14_sortEmployeesByAge() {
        List<Employee> result = knockout.sortEmployeesByAge(employees);
        assertEquals(8, result.size());
        assertEquals("Henry", result.get(0).getName()); // 26
        assertEquals("Alice", result.get(1).getName()); // 28
        assertEquals("Grace", result.get(7).getName()); // 45
    }

    // Level 5: 発展

    @Test
    void problem15_employeeNamesByDepartment() {
        Map<String, List<String>> result = knockout.employeeNamesByDepartment(employees);
        assertEquals(3, result.size());
        assertEquals(4, result.get("Engineering").size());
        assertTrue(result.get("Engineering").contains("Alice"));
        assertTrue(result.get("Sales").contains("Bob"));
    }

    @Test
    void problem16_averageAgeOfProjectMembers() {
        Project project = new Project(1L, "Project Alpha", List.of(1L, 3L, 5L)); // Alice(28), Charlie(42), Eve(31)
        double avg = knockout.averageAgeOfProjectMembers(employees, project);
        assertEquals(33.67, avg, 0.01); // (28+42+31)/3 = 33.67
    }

    @Test
    void problem17_calculateMedianSalary() {
        double median = knockout.calculateMedianSalary(employees);
        assertEquals(53000.0, median, 0.01); // 8個のデータ: 中央2つ(51000, 55000)の平均 = 53000
    }

    // Level 6: 最難関

    @Test
    void problem18_salaryRangeByDepartment() {
        Map<String, Double> result = knockout.salaryRangeByDepartment(employees);
        assertEquals(3, result.size());
        assertEquals(25000.0, result.get("Engineering")); // 75000 - 50000
        assertEquals(3000.0, result.get("Sales")); // 51000 - 48000
        assertEquals(16000.0, result.get("HR")); // 58000 - 42000
    }

    @Test
    void problem19_countByAgeGroup() {
        Map<String, Long> result = knockout.countByAgeGroup(employees);
        assertEquals(3, result.size());
        assertEquals(3L, result.get("20代")); // Alice(28), Dave(29), Henry(26)
        assertEquals(3L, result.get("30代")); // Bob(35), Eve(31), Frank(38)
        assertEquals(2L, result.get("40代")); // Charlie(42), Grace(45)
    }

    @Test
    void problem20_findEmployeesInMultipleProjects() {
        List<Project> projects = List.of(
                new Project(1L, "Project Alpha", List.of(1L, 3L, 5L)),
                new Project(2L, "Project Beta", List.of(1L, 2L, 4L)),
                new Project(3L, "Project Gamma", List.of(3L, 5L, 6L)));
        List<Employee> result = knockout.findEmployeesInMultipleProjects(employees, projects);
        assertEquals(3, result.size()); // Alice(1), Charlie(3), Eve(5) appear multiple times
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("Alice")));
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("Charlie")));
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("Eve")));
    }
}
