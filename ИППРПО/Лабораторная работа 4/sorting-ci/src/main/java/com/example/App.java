package com.example;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        int[] values = {7, -2, 4, 7, 0};
        System.out.println("Input: " + Arrays.toString(values));
        System.out.println("Sorted: " + Arrays.toString(ArraySorter.sort(values)));
    }
}
