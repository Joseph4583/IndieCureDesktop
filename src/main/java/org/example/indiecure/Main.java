package org.example.indiecure;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("SessionLoginScreen.fxml"));
        Parent parent = fxmlLoader.load();
        SessionLoginScreen sessionLoginScreen = fxmlLoader.getController();
        Scene scene = new Scene(parent, 800, 600);
        stage.setTitle("IndieCure");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        sessionLoginScreen.setStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}