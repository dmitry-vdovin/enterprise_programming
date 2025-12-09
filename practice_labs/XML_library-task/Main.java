package org.example.libraryapp_task;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library.fxml"));  // СЛЭШ ВПЕРЕДИ!

        if (loader.getLocation() == null) {
            // FXML не найден - показываем заглушку
            Label label = new Label("FXML file not found! Put library.fxml in src/main/resources/");
            StackPane root = new StackPane(label);
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
            return;
        }

        Scene scene = new Scene(loader.load(), 800, 600);
        stage.setTitle("LibraryApp_task");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
