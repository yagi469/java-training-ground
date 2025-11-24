package com.example.week2.StreamAPIBook;

import java.util.stream.Stream;

public class Program3_iterate2 {
    public static void main(String[] args) {
        System.out.println("--- Start ---");
        Stream.iterate("a", i -> i + "b")
                .limit(5)
                .forEach(System.out::println);
    }
}
