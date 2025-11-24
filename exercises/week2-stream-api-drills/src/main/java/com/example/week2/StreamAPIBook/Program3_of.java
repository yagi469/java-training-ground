package com.example.week2.StreamAPIBook;

import java.util.stream.Stream;

public class Program3_of {
    public static void main(String[] args) {
        var stream = Stream.of("ピチュー", "ピカチュウ", "ライチュウ");
        for (var item : stream.toArray()) {
            System.out.println(item);
        }
    }
}
