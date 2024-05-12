package org.example.indiecure_aplication.Model.Classes;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Controller.*;
import org.example.indiecure_aplication.Main;

import java.io.IOException;

public class ScreenSwitcher {
    HomeScreen homeScreen;
    PacientScreen pacientScreen;
    SymptomScreen symptomScreen;
    IllnessScreen illnessScreen;
    DiagnosticScreen diagnosticScreen;
    IllnessTestingScreen illnessTestingScreen;
    IllnessDiagnosisScreen illnessDiagnosisScreen;

    SessionLoginScreen sessionLoginScreen;



    public void screenSwitch (String archivo, Stage stage, Doctor doctor){
        try {
            stage.setTitle("IndieCure");
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("View/Screens/" + archivo));
            Scene scene = new Scene(loader.load());
            switch (archivo) {
                case "HomeScreen.fxml": homeScreen = loader.getController(); homeScreen.setStage(stage); homeScreen.setDoctor(doctor); break;
                case "PacientScreen.fxml": pacientScreen = loader.getController(); pacientScreen.setStage(stage); pacientScreen.setDoctor(doctor); break;
                case "SymptomScreen.fxml":
                    symptomScreen = loader.getController();
                    symptomScreen.setStage(stage);
                    symptomScreen.setDoctor(doctor);
                    break;
                case "IllnessScreen.fxml":
                    illnessScreen = loader.getController();
                    illnessScreen.setStage(stage);
                    illnessScreen.setDoctor(doctor);
                    break;
                case "DiagnosticScreen.fxml":
                    diagnosticScreen = loader.getController();
                    diagnosticScreen.setStage(stage);
                    diagnosticScreen.setDoctor(doctor);
                    break;
                case "IllnessDiagnosisScreen.fxml":
                    illnessDiagnosisScreen = loader.getController();
                    illnessDiagnosisScreen.setStage(stage);
                    illnessDiagnosisScreen.setDoctor(doctor);
                    break;
                case "IllnessTestingScreen.fxml":
                    illnessTestingScreen = loader.getController();
                    illnessTestingScreen.setStage(stage);
                    illnessTestingScreen.setDoctor(doctor);
                    break;
                default: System.out.print("Error al pasar archivo");
            }
            stage.setScene(scene);
            stage.show();
            stage.setMaximized(true);
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void LogOff(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/SessionLoginScreen.fxml"));
            Parent root = fxmlLoader.load();
            SessionLoginScreen controller = fxmlLoader.getController();
            Rectangle2D screenBounds = Screen.getPrimary().getBounds();
            controller.setStage(stage);
            Scene scene = new Scene(root, 320, 240);
            stage.setTitle("IndieCure");
            stage.setScene(scene);
            stage.setHeight(screenBounds.getHeight());
            stage.setWidth(screenBounds.getWidth());
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
