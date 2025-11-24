package com.example.week2.StreamAPIBook;

import java.util.Arrays;

// StreamAPIを使用して、配列の4つ目から6つ目までの要素を 
// 掛け算した出力結果となるようにしてください。

public class Program2_1 {
    public static void main(String[] args) {
        int[] array = new int[] {1, 2, 3, 4, 5, 6};
        int suuti = Arrays.stream(array)
                .skip(3) // 4つ目の要素から開始
                .limit(3) // 4つ目、5つ目、6つ目の要素を取得
                .reduce(1, (a, b) -> a * b); // 要素を掛け合わせる
        System.out.println(suuti);
    }
}
