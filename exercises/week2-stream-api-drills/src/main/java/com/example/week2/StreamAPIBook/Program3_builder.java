package com.example.week2.StreamAPIBook;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

public class Program3_builder {
    public static void main(String[] args) {
        Builder<String> builder = Stream.builder();
        Stream<String> stream = builder.add("ピチュー").add("ピカチュウ")
                .add("ライチュウ").build();
        for (var item : stream.toArray()) {
            System.out.println(item);
        }
    }
}
