package com.example.week2.StreamAPIBook;

import java.util.stream.Stream;

public class Program3_generate {
    public static void main(String[] args) {
        System.out.println("--- Start ---");
        Stream.generate(() -> "test")
                .limit(5)
                .forEach(System.out::println);
    }
}
