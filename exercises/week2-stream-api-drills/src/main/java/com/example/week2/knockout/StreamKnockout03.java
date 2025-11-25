package com.example.week2.knockout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamKnockout03 {

    // Level 7: 実務レベル

    public double calculateTotalOrderAmount(List<Order> orders) {
        // Problem 21 - 注文の合計金額を計算
        return orders.stream()
                .mapToDouble(order -> order.getPrice() * order.getQuantity())
                .sum();
    }

    public Map<Long, Double> totalOrderAmountByEmployee(List<Order> orders) {
        // Problem 22 - 従業員ごとの注文総額を計算
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getEmployeeId,
                        Collectors.summingDouble(order -> order.getPrice() * order.getQuantity())));
    }

    public List<Order> findOrdersBetweenDates(List<Order> orders, LocalDate startDate, LocalDate endDate) {
        // Problem 23 - 特定期間の注文をフィルタ
        return orders.stream()
                .filter(order -> order.getOrderDate().isAfter(startDate) && order.getOrderDate().isBefore(endDate))
                .collect(Collectors.toList());
    }

    public Map<String, Integer> totalQuantityByProduct(List<Order> orders) {
        // Problem 24 - 商品ごとの販売数量を集計
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getProduct,
                        Collectors.summingInt(Order::getQuantity)));
    }

    // Level 8: エキスパート

    public Optional<Employee> findTopSalesEmployee(List<Employee> employees, List<Order> orders) {
        // Problem 25 - 最も売上の高い従業員を検索
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getEmployeeId,
                        Collectors.summingDouble(order -> order.getPrice() * order.getQuantity())))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> employees.stream()
                        .filter(employee -> employee.getId().equals(entry.getKey()))
                        .findFirst()
                        .orElse(null));
    }

    public Map<String, List<String>> top3ProductsByDepartment(List<Employee> employees, List<Order> orders) {
        // Problem 26 - 部門ごとの売上トップ3商品を取得
        // employeeIdから部門を取得するためのMapを作成
        Map<Long, String> employeeDepartmentMap = employees.stream()
                .collect(Collectors.toMap(Employee::getId, Employee::getDepartment));

        return orders.stream()
                // employeeIdから部門を取得してグルーピング
                .collect(Collectors.groupingBy(
                        order -> employeeDepartmentMap.get(order.getEmployeeId()),
                        Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                                .sorted(Comparator
                                        .comparingDouble((Order order) -> order.getPrice() * order.getQuantity())
                                        .reversed())
                                .limit(3)
                                .map(Order::getProduct)
                                .collect(Collectors.toList()))));
    }

    public Map<String, Double> monthlySales(List<Order> orders) {
        // Problem 27 - 月ごとの売上推移を計算
        return orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.summingDouble(order -> order.getPrice() * order.getQuantity())));
    }

    // Level 9: マスター

    public List<Employee> findAboveAverageSalesEmployees(List<Employee> employees, List<Order> orders) {
        // Problem 28 - 全従業員の平均売上を超える従業員を抽出
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getEmployeeId,
                        Collectors.summingDouble(order -> order.getPrice() * order.getQuantity())))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > orders.stream()
                        .collect(Collectors.groupingBy(Order::getEmployeeId,
                                Collectors.summingDouble(order -> order.getPrice() * order.getQuantity())))
                        .entrySet().stream()
                        .mapToDouble(Map.Entry::getValue)
                        .average()
                        .orElse(0.0))
                .map(entry -> employees.stream()
                        .filter(employee -> employee.getId().equals(entry.getKey()))
                        .findFirst()
                        .orElse(null))
                .collect(Collectors.toList());
    }

    public Optional<LocalDate> findBusiestOrderDate(List<Order> orders) {
        // Problem 29 - 最も注文が集中した日を特定
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getOrderDate,
                        Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public List<Employee> createSalesRanking(List<Employee> employees, List<Order> orders) {
        // Problem 30 - 従業員の売上ランキングを作成
        // 従業員ごとの売上を事前に計算
        Map<Long, Double> salesByEmployee = orders.stream()
                .collect(Collectors.groupingBy(Order::getEmployeeId,
                        Collectors.summingDouble(order -> order.getPrice() * order.getQuantity())));

        return employees.stream()
                .sorted(Comparator
                        .comparingDouble((Employee employee) -> salesByEmployee.getOrDefault(employee.getId(), 0.0))
                        .reversed())
                .collect(Collectors.toList());
    }
}
