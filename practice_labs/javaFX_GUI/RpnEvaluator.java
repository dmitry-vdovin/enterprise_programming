package com.example.rpncalculator2;

import java.util.ArrayDeque;
import java.util.Deque;

public class RpnEvaluator {

    /**
     * Вычисляет выражение в обратной польской записи.
     * Токены разделяются пробелами.
     * Поддержка: числа (double), +, -, *, /, ^, унарный neg.
     */
    public static double evaluate(String expr) {
        if (expr == null || expr.isBlank()) {
            throw new IllegalArgumentException("пустое выражение");
        }

        Deque<Double> stack = new ArrayDeque<>();
        String[] tokens = expr.trim().split("\\s+");

        for (String t : tokens) {
            switch (t) {
                case "+" -> {
                    requireSize(stack, 2, t);
                    double b = stack.pop(), a = stack.pop();
                    stack.push(a + b);
                }
                case "-" -> {
                    requireSize(stack, 2, t);
                    double b = stack.pop(), a = stack.pop();
                    stack.push(a - b);
                }
                case "*" -> {
                    requireSize(stack, 2, t);
                    double b = stack.pop(), a = stack.pop();
                    stack.push(a * b);
                }
                case "/" -> {
                    requireSize(stack, 2, t);
                    double b = stack.pop(), a = stack.pop();
                    if (b == 0.0) {
                        throw new IllegalArgumentException("деление на ноль");
                    }
                    stack.push(a / b);
                }
                case "^" -> {
                    requireSize(stack, 2, t);
                    double b = stack.pop(), a = stack.pop();
                    stack.push(Math.pow(a, b));
                }
                case "neg" -> { // унарный минус
                    requireSize(stack, 1, t);
                    double a = stack.pop();
                    stack.push(-a);
                }
                default -> {
                    try {
                        double v = Double.parseDouble(t);
                        stack.push(v);
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException("неизвестный токен: " + t);
                    }
                }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("некорректное выражение (осталось " + stack.size() + " знач.)");
        }
        return stack.pop();
    }

    private static void requireSize(Deque<Double> stack, int n, String op) {
        if (stack.size() < n) {
            throw new IllegalArgumentException("операции \"" + op + "\" не хватает операндов");
        }
    }
}