package org.example.indiecure_aplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Controller.SessionLoginScreen;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        //loads the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/SessionLoginScreen.fxml"));
        Parent root = fxmlLoader.load();
        SessionLoginScreen controller = fxmlLoader.getController();

        //gets the screen size so the app stars with the user screen size set.
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        controller.setStage(stage);
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("IndieCure");
        stage.setScene(scene);
        stage.setHeight(screenBounds.getHeight());
        stage.setWidth(screenBounds.getWidth());

        //shows the screen
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}