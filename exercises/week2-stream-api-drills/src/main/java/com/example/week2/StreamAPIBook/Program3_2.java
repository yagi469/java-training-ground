package com.example.week2.StreamAPIBook;

import java.util.stream.Stream;

public class Program3_2 {
    public static void main(String[] args) {
        System.out.println("--- Start ---");
        Stream.iterate(2, i -> i * 2)
                .limit(10)
                .forEach(System.out::println);
    }
}
