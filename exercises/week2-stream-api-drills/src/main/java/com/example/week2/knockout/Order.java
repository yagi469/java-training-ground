package com.example.week2.knockout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Long employeeId;
    private String product;
    private int quantity;
    private double price;
    private LocalDate orderDate;
}
