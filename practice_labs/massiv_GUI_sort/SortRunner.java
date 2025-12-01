package com.example.massiv_gui_sort.ui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class SortRunner implements Runnable {
    private final String name;
    private final int[] original;
    private final VBox barsBox;
    private final Label startLbl;
    private final Label endLbl;
    private final BiConsumer<int[], Consumer<int[]>> sorter;
    private final int stepDelayMs;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public SortRunner(
            String name,
            int[] data,
            VBox barsBox,
            Label startLbl,
            Label endLbl,
            BiConsumer<int[], Consumer<int[]>> sorter,
            int stepDelayMs
    ) {
        this.name = name;
        this.original = data;
        this.barsBox = barsBox;
        this.startLbl = startLbl;
        this.endLbl = endLbl;
        this.sorter = sorter;
        this.stepDelayMs = stepDelayMs;
    }

    @Override
    public void run() {
        int[] a = Arrays.copyOf(original, original.length);

        Platform.runLater(() -> {
            startLbl.setText("Старт: " + LocalTime.now().format(fmt));
            drawBars(a);
        });

        Consumer<int[]> onStep = snapshot ->
                Platform.runLater(() -> drawBars(snapshot));

        try {
            sorter.accept(a, onStep); // сортируем, сообщая о шагах
        } catch (Exception e) {
            Platform.runLater(() -> endLbl.setText("Ошибка: " + e.getMessage()));
            return;
        }

        Platform.runLater(() ->
                endLbl.setText("Финиш: " + LocalTime.now().format(fmt)));
    }

    private void drawBars(int[] data) {
        barsBox.getChildren().clear();
        if (data.length == 0) return;

        double boxWidth = barsBox.getWidth() > 0 ? barsBox.getWidth() : 400;
        int max = Arrays.stream(data).max().orElse(1);
        if (max <= 0) max = 1;

        // отступ справа под текст значения
        double textArea = 60;

        for (int v : data) {
            double w = (v <= 0 ? 0 : (v * 1.0 / max) * Math.max(20, (boxWidth - textArea)));
            Rectangle bar = new Rectangle(w, 18);
            bar.getStyleClass().add("bar");

            Text value = new Text(String.valueOf(v));
            value.getStyleClass().add("bar-label");

            javafx.scene.layout.HBox row = new javafx.scene.layout.HBox(8, bar, value);
            row.getStyleClass().add("bar-row");
            barsBox.getChildren().add(row);
        }
    }

    // фабрики «сортировщиков» с задержкой
    public static BiConsumer<int[], Consumer<int[]>> bubble(int delayMs) {
        return (arr, onStep) -> {
            try { SortAlgorithms.bubbleSort(arr, onStep, delayMs); }
            catch (InterruptedException ignored) {}
        };
    }
    public static BiConsumer<int[], Consumer<int[]>> insertion(int delayMs) {
        return (arr, onStep) -> {
            try { SortAlgorithms.insertionSort(arr, onStep, delayMs); }
            catch (InterruptedException ignored) {}
        };
    }
    public static BiConsumer<int[], Consumer<int[]>> quick(int delayMs) {
        return (arr, onStep) -> {
            try { SortAlgorithms.quickSort(arr, onStep, delayMs); }
            catch (InterruptedException ignored) {}
        };
    }
}