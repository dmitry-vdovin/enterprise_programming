package com.example.massiv_gui_sort.ui;

import java.util.function.Consumer;

public final class SortAlgorithms {
    private SortAlgorithms() {}

    public static void bubbleSort(int[] a, Consumer<int[]> onStep, int delayMs) throws InterruptedException {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (a[j] > a[j+1]) {
                    swap(a, j, j+1);
                    swapped = true;
                    onStep.accept(a.clone());
                    Thread.sleep(delayMs);
                }
            }
            if (!swapped) break;
        }
    }

    public static void insertionSort(int[] a, Consumer<int[]> onStep, int delayMs) throws InterruptedException {
        for (int i = 1; i < a.length; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];
                j--;
                onStep.accept(a.clone());
                Thread.sleep(delayMs);
            }
            a[j + 1] = key;
            onStep.accept(a.clone());
            Thread.sleep(delayMs);
        }
    }

    public static void quickSort(int[] a, Consumer<int[]> onStep, int delayMs) throws InterruptedException {
        quickSortRec(a, 0, a.length - 1, onStep, delayMs);
    }

    private static void quickSortRec(int[] a, int l, int r, Consumer<int[]> onStep, int delayMs) throws InterruptedException {
        if (l >= r) return;
        int i = l, j = r;
        int pivot = a[(l + r) >>> 1];
        while (i <= j) {
            while (a[i] < pivot) i++;
            while (a[j] > pivot) j--;
            if (i <= j) {
                if (i != j) {
                    swap(a, i, j);
                    onStep.accept(a.clone());
                    Thread.sleep(delayMs);
                }
                i++; j--;
            }
        }
        if (l < j) quickSortRec(a, l, j, onStep, delayMs);
        if (i < r) quickSortRec(a, i, r, onStep, delayMs);
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }
}