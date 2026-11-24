package com.example;

import java.util.Objects;

public final class ArraySorter {
    private ArraySorter() {}

    public static int[] sort(int[] values) {
        int[] result = Objects.requireNonNull(values, "values").clone();
        for (int end = result.length - 1; end > 0; end--) {
            boolean changed = false;
            for (int i = 0; i < end; i++) {
                if (result[i] > result[i + 1]) {
                    int value = result[i];
                    result[i] = result[i + 1];
                    result[i + 1] = value;
                    changed = true;
                }
            }
            if (!changed) break;
        }
        return result;
    }
}
