package com.example.week2.day2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectorsDrill {

    public Map<String, List<Product>> groupByCategory(List<Product> products) {
        // Implement this method using Collectors.groupingBy
        return products.stream()
                .collect(Collectors.groupingBy(Product::getCategory));
    }

    public String joinProductNames(List<Product> products) {
        // Implement this method using Collectors.joining
        return products.stream()
                .map(Product::getName)
                .collect(Collectors.joining(", "));
    }

    public Map<String, Long> countByCategory(List<Product> products) {
        // Implement this method using Collectors.groupingBy and
        // Collectors.counting
        return products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));
    }
}
