package com.example.massiv_gui_sort.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public final class ArraysInput {
    public static int[] arr1;
    public static int[] arr2;
    public static int[] arr3;

    private ArraysInput() {}

    public static void readAllFromConsoleOrExit() {
        System.out.println("Введите 3 массива (каждый в своей строке). Числа — целые, через пробел.");
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);

        arr1 = readOne(sc, 1);
        arr2 = readOne(sc, 2);
        arr3 = readOne(sc, 3);

        System.out.println("Принято. Сортировки будут визуализированы в окне.");
    }

    private static int[] readOne(Scanner sc, int idx) {
        while (true) {
            System.out.print("Массив " + idx + ": ");
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("Строка пустая. Повторите ввод.");
                continue;
            }
            String[] parts = line.split("\\s+");
            List<Integer> vals = new ArrayList<>();
            try {
                for (String p : parts) vals.add(Integer.parseInt(p));
            } catch (NumberFormatException ex) {
                System.out.println("Можно вводить только целые числа. Повторите ввод.");
                continue;
            }
            if (vals.isEmpty()) {
                System.out.println("Нет чисел. Повторите ввод.");
                continue;
            }
            int[] a = new int[vals.size()];
            for (int i = 0; i < a.length; i++) a[i] = vals.get(i);
            return a;
        }
    }
}