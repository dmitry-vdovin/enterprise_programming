package com.example.massiv_gui_sort;

import com.example.massiv_gui_sort.util.ArraysInput;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/com/example/massiv_gui_sort/main-view.fxml"));
        Scene scene = new Scene(fxml.load(), 1000, 600);
        stage.setTitle("Визуализация сортировок (3 потока)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // читаем три массива из консоли ДО запуска JavaFX
        // Формат: одна строка на массив, числа через пробел (например: 5 3 9 1 2)
        ArraysInput.readAllFromConsoleOrExit();
        launch(args);
    }
}
