package org.example.indiecure;

import Classes.ScreenSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class IllnessTestingScreen {
    Stage stage;
    ScreenSwitcher screenSwitcher = new ScreenSwitcher();


    ScreenSwitcher switcher = new ScreenSwitcher();
    public void switchScreenToHome(ActionEvent actionEvent) {

        switcher.screenSwitch("HomeScreen.fxml", stage);
    }

    public void switchScreenToPacient(ActionEvent actionEvent) {
        switcher.screenSwitch("PacientScreen.fxml", stage);
    }

    public void switchScreenToIllnessDiagnosis(ActionEvent actionEvent) {
        switcher.screenSwitch("IllnessDiagnosisScreen.fxml", stage);
    }

    public void switchScreenToIllnesstesting(ActionEvent actionEvent) {
        switcher.screenSwitch("IllnessTestingScreen.fxml", stage);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
