package com.example.rpncalculator2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        TextField input = new TextField();
        input.setPromptText("пример: 3 4 + 2 *");

        Button calcBtn = new Button("Вычислить");
        Label resultLabel = new Label("Результат: —");

        Label help = new Label("""
                Операции:
                 + (сложение)
                 - (вычитание)
                 * (умножение)
                 / (деление)
                 ^ (степень)
                 neg (смена знака)
                Пример: 3 4 + 2 *  => 14
                """);
        help.setStyle("-fx-opacity: 0.85; -fx-font-size: 11px;");

        calcBtn.setDefaultButton(true);
        calcBtn.setOnAction(e -> {
            String expr = input.getText().trim();
            if (expr.isEmpty()) {
                resultLabel.setText("Результат: —");
                return;
            }
            try {
                double value = RpnEvaluator.evaluate(expr);
                resultLabel.setText("Результат: " + stripTrailingZeros(value));
            } catch (IllegalArgumentException ex) {
                resultLabel.setText("Ошибка: " + ex.getMessage());
            }
        });

        VBox root = new VBox(10,
                new Label("Введите выражение в обратной польской записи (RPN):"),
                input,
                new HBox(10, calcBtn, resultLabel),
                help
        );
        root.setPadding(new Insets(16));

        stage.setTitle("RPN Calculator (JavaFX)");
        stage.setScene(new Scene(root, 420, 260));
        stage.show();
    }

    private static String stripTrailingZeros(double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) return String.valueOf(d);
        String s = String.valueOf(d);
        if (s.contains("E") || s.contains("e")) return s;
        if (s.endsWith(".0")) return s.substring(0, s.length() - 2);
        return s;
    }

    public static void main(String[] args) {
        launch();
    }
}