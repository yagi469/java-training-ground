package com.example.week2.StreamAPIBook;

import java.util.stream.Stream;

public class Program3_iterate {
    public static void main(String[] args) {
        System.out.println("--- Start ---");
        Stream.iterate(1, i -> i + 1)
                .limit(5)
                .forEach(System.out::println);
    }
}
