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

/**
 * this class handles the screen switches for using the same state and dont open a new window
 */
public class ScreenSwitcher {
    HomeScreen homeScreen;
    PacientScreen pacientScreen;
    SymptomScreen symptomScreen;
    IllnessScreen illnessScreen;
    DiagnosticScreen diagnosticScreen;
    IllnessTestingScreen illnessTestingScreen;
    IllnessDiagnosisScreen illnessDiagnosisScreen;

    SessionLoginScreen sessionLoginScreen;


    /**
     * Handles the switches between screens base on the file
     * @param file of the FXML path
     * @param stage of the window
     * @param doctor to check the permissions
     */
    public void screenSwitch (String file, Stage stage, Doctor doctor){
        try {
            stage.setTitle("IndieCure");
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("View/Screens/" + file));
            Scene scene = new Scene(loader.load());
            switch (file) {
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
                default: System.out.print("Error al pasar file");
            }
            stage.setScene(scene);
            stage.show();
            stage.setMaximized(true);
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * close the user session.
     * @param stage
     */
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
