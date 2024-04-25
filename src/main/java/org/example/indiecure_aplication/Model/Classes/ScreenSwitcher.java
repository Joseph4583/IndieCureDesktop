package org.example.indiecure_aplication.Model.Classes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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


    public void screenSwitch (String archivo, Stage stage){
        try {
            stage.setTitle("IndieCure");
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("View/Screens/" + archivo));
            BorderPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            switch (archivo) {
                case "HomeScreen.fxml": homeScreen = loader.getController(); homeScreen.setStage(stage); break;
                case "PacientScreen.fxml": pacientScreen = loader.getController(); pacientScreen.setStage(stage); pacientScreen.startPacientList(); break;
                case "SymptomScreen.fxml": symptomScreen = loader.getController(); symptomScreen.setStage(stage); symptomScreen.startSymptomList(); break;
                case "IllnessScreen.fxml": illnessScreen = loader.getController(); illnessScreen.setStage(stage); illnessScreen.startIllnessList(); break;
                case "DiagnosticScreen.fxml": diagnosticScreen = loader.getController(); diagnosticScreen.setStage(stage); diagnosticScreen.startDiagnosticList(); break;
                case "IllnessDiagnosisScreen.fxml": illnessDiagnosisScreen = loader.getController(); illnessDiagnosisScreen.setStage(stage); break;
                case "IllnessTestingScreen.fxml": illnessTestingScreen = loader.getController(); illnessTestingScreen.setStage(stage); break;
                default: System.out.print("Error al pasar archivo");
            }
            stage.show();
            stage.setMaximized(true);
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
