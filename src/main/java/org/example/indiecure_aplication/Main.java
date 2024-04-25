package org.example.indiecure_aplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Controller.SessionLoginScreen;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/SessionLoginScreen.fxml"));
        Parent root = fxmlLoader.load();
        SessionLoginScreen controller = fxmlLoader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("IndieCure");
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch();
    }
}