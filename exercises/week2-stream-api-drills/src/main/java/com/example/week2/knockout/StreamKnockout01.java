package com.example.week2.knockout;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

public class StreamKnockout01 {

    // Level 1: 基礎

    public List<Employee> findEmployeesOver30(List<Employee> employees) {
        // Problem 1 - 30歳以上の従業員を抽出
        return employees.stream()
                .filter(e -> e.getAge() > 30)
                .toList();
    }

    public List<String> getAllNames(List<Employee> employees) {
        // Problem 2 - 全従業員の名前リストを作成
        return employees.stream()
                .map(Employee::getName)
                .toList();
    }

    public long countEngineeringEmployees(List<Employee> employees) {
        // Problem 3 - Engineering部門の従業員数をカウント
        return employees.stream()
                .filter(e -> e.getDepartment().equals("Engineering"))
                .count();
    }

    // Level 2: 中級

    public Map<String, List<Employee>> groupByDepartment(List<Employee> employees) {
        // Problem 4 - 部門ごとに従業員をグループ化
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

    public String joinHighEarnerNames(List<Employee> employees) {
        // Problem 5 - 給与が50000以上の従業員の名前をカンマ区切りで結合
        return employees.stream()
                .filter(e -> e.getSalary() >= 50000)
                .map(Employee::getName)
                .collect(Collectors.joining(","));
    }

    public Map<String, Double> averageSalaryByDepartment(List<Employee> employees) {
        // Problem 6 - 部門ごとの平均給与を計算
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getSalary)));
    }

    public Optional<Employee> findHighestPaidEmployee(List<Employee> employees) {
        // Problem 7 - 最も給与の高い従業員を検索
        return employees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary));
    }

    // Level 3: 応用

    public Map<String, Optional<Employee>> findTopEarnerPerDepartment(List<Employee> employees) {
        // Problem 8 - 各部門で最も給与の高い従業員を取得
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary))));
    }

    public List<Employee> findTop3HighestPaid(List<Employee> employees) {
        // Problem 9 - 給与上位3名の従業員を取得
        return employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .limit(3)
                .toList();
    }

    public List<String> findDepartmentsWithHighTotalSalary(List<Employee> employees) {
        // Problem 10 - 部門ごとの給与合計が100000を超える部門名のリストを取得
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.summingDouble(Employee::getSalary)))
                .entrySet().stream()
                .filter(e -> e.getValue() >= 100000)
                .map(Map.Entry::getKey)
                .toList();
    }
}
