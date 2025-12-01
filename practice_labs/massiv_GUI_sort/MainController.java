package com.example.massiv_gui_sort.ui;

import com.example.massiv_gui_sort.util.ArraysInput;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML private Button startBtn;

    // контейнеры и метки для 3 потоков
    @FXML private VBox boxInsert;
    @FXML private Label insertStart;
    @FXML private Label insertEnd;

    @FXML private VBox boxBubble;
    @FXML private Label bubbleStart;
    @FXML private Label bubbleEnd;

    @FXML private VBox boxQuick;
    @FXML private Label quickStart;
    @FXML private Label quickEnd;

    // задержка между шагами (мс) — визуальная скорость
    private static final int STEP_DELAY_MS = 100;

    @FXML
    private void initialize() {
        // при старте просто покажем исходные массивы как полосы (до нажатия Start)
    }

    @FXML
    private void onStart() {
        startBtn.setDisable(true);

        // три независимых «раннера» (потока) — разные алгоритмы
        Thread t1 = new Thread(new SortRunner(
                "Insertion Sort",
                ArraysInput.arr1,
                boxInsert,
                insertStart,
                insertEnd,
                SortRunner.insertion(STEP_DELAY_MS),
                STEP_DELAY_MS
        ), "Insertion-Thread");
        t1.setDaemon(true);

        Thread t2 = new Thread(new SortRunner(
                "Bubble Sort",
                ArraysInput.arr2,
                boxBubble,
                bubbleStart,
                bubbleEnd,
                SortRunner.bubble(STEP_DELAY_MS),
                STEP_DELAY_MS
        ), "Bubble-Thread");
        t2.setDaemon(true);

        Thread t3 = new Thread(new SortRunner(
                "Quick Sort",
                ArraysInput.arr3,
                boxQuick,
                quickStart,
                quickEnd,
                SortRunner.quick(STEP_DELAY_MS),
                STEP_DELAY_MS
        ), "Quick-Thread");
        t3.setDaemon(true);

        t1.start();
        t2.start();
        t3.start();
    }
}