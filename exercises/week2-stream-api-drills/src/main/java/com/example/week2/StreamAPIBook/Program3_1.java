package com.example.week2.StreamAPIBook;

import java.util.stream.Stream;

public class Program3_1 {
    public static void main(String[] args) {
        var content = "ピカチュウ";
        System.out.println("--- Start ---");
        Stream.generate(() -> content)
                .limit(5)
                .forEach(System.out::println);
    }
}
