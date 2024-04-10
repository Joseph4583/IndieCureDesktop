package Classes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.indiecure.*;

import java.io.IOException;

public class ScreenSwitcher {
    HomeScreen homeScreen;
    PacientScreen pacientScreen;
    IllnessTestingScreen illnessTestingScreen;
    IllnessDiagnosisScreen illnessDiagnosisScreen;

    public void screenSwitch (String archivo, Stage stage){
        try {
            stage.setTitle("IndieCure");
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(archivo));
            BorderPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.setMaximized(true);
            switch (archivo) {
                case "HomeScreen.fxml": homeScreen = loader.getController(); homeScreen.setStage(stage); break;
                case "PacientScreen.fxml": pacientScreen = loader.getController(); pacientScreen.setStage(stage); break;
                case "IllnessDiagnosisScreen.fxml": illnessDiagnosisScreen = loader.getController(); illnessDiagnosisScreen.setStage(stage); break;
                case "IllnessTestingScreen.fxml": illnessTestingScreen = loader.getController(); illnessTestingScreen.setStage(stage); break;
                default: System.out.print("Error al pasar archivo");
            }
            stage.show();

        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
