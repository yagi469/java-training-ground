package com.example.week2.knockout;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamKnockout02 {

    // Level 4: 実践

    public double calculateTotalSalary(List<Employee> employees) {
        // Problem 11 - 給与の合計を計算
        return employees.stream()
                .mapToDouble(Employee::getSalary)
                .sum();
    }

    public Map<String, Double> maxSalaryByDepartment(List<Employee> employees) {
        // Problem 12 - 部門ごとの最高給与を取得
        return employees.stream()
                .collect(Collectors.toMap(
                        Employee::getDepartment, // Key: 部門
                        Employee::getSalary, // Value: 給与
                        (s1, s2) -> Math.max(s1, s2) // Merge: 高い方を採用
                ));
    }

    public List<Employee> findEmployeesWithNameContaining(List<Employee> employees, String keyword) {
        // Problem 13 - 名前に特定の文字列を含む従業員をフィルタ
        return employees.stream()
                .filter(e -> e.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Employee> sortEmployeesByAge(List<Employee> employees) {
        // Problem 14 - 年齢で昇順ソートした従業員リストを取得
        return employees.stream()
                .sorted(Comparator.comparingInt(Employee::getAge))
                .collect(Collectors.toList());
    }

    // Level 5: 発展

    public Map<String, List<String>> employeeNamesByDepartment(List<Employee> employees) {
        // Problem 15 - 各部門の従業員名リストを作成
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.mapping(Employee::getName, Collectors.toList())));
    }

    public double averageAgeOfProjectMembers(List<Employee> employees, Project project) {
        // Problem 16 - プロジェクトメンバーの平均年齢を計算
        return employees.stream()
                .filter(e -> project.getMemberIds().contains(e.getId()))
                .mapToDouble(Employee::getAge)
                .average()
                .orElse(0.0);
    }

    public double calculateMedianSalary(List<Employee> employees) {
        // Problem 17 - 給与中央値を計算
        int size = employees.size();
        if (size == 0)
            return 0.0;

        return employees.stream()
                .mapToDouble(Employee::getSalary)
                .sorted()
                .skip((size - 1) / 2)
                .limit(2 - size % 2) // 奇数なら1個、偶数なら2個
                .average()
                .orElse(0.0);
    }

    // Level 6: 最難関

    public Map<String, Double> salaryRangeByDepartment(List<Employee> employees) {
        // Problem 18 - 部門ごとの給与範囲（最大-最小）を計算
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.collectingAndThen(
                                Collectors.summarizingDouble(Employee::getSalary),
                                stats -> stats.getMax() - stats.getMin())));
    }

    public Map<String, Long> countByAgeGroup(List<Employee> employees) {
        // Problem 19 - 年齢層（10歳刻み）ごとの人数を集計
        return employees.stream()
                .collect(Collectors.groupingBy(
                        e -> (e.getAge() / 10 * 10) + "代",
                        Collectors.counting()));
    }

    public List<Employee> findEmployeesInMultipleProjects(List<Employee> employees, List<Project> projects) {
        // Problem 20 - 複数プロジェクトに参加している従業員を検索
        Map<Long, Long> participationCounts = projects.stream()
                .flatMap(p -> p.getMemberIds().stream())
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

        return employees.stream()
                .filter(e -> participationCounts.getOrDefault(e.getId(), 0L) > 1)
                .collect(Collectors.toList());
    }
}
