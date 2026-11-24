package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArraySorterTest {
    @Test void empty() { assertArrayEquals(new int[]{}, ArraySorter.sort(new int[]{})); }
    @Test void oneElement() { assertArrayEquals(new int[]{4}, ArraySorter.sort(new int[]{4})); }
    @Test void duplicatesAndNegatives() { assertArrayEquals(new int[]{-2, 0, 7, 7}, ArraySorter.sort(new int[]{7, -2, 7, 0})); }
    @Test void alreadySorted() { assertArrayEquals(new int[]{1, 2, 3}, ArraySorter.sort(new int[]{1, 2, 3})); }
    @Test void reverseOrder() { assertArrayEquals(new int[]{1, 2, 3, 4}, ArraySorter.sort(new int[]{4, 3, 2, 1})); }
    @Test void extremes() { assertArrayEquals(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, ArraySorter.sort(new int[]{Integer.MAX_VALUE, 0, Integer.MIN_VALUE})); }
    @Test void nullIsRejected() { assertThrows(NullPointerException.class, () -> ArraySorter.sort(null)); }
    @Test void inputIsNotModified() { int[] source = {2, 1}; ArraySorter.sort(source); assertArrayEquals(new int[]{2, 1}, source); }
}
