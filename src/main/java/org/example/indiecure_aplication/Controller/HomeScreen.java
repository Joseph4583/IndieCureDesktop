package org.example.indiecure_aplication.Controller;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Model.Classes.ScreenSwitcher;


public class HomeScreen {
    Stage stage;
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
    public void switchScreenToSymptoms(ActionEvent actionEvent) {
        switcher.screenSwitch("SymptomScreen.fxml", stage);
    }

    public void switchScreenToIllness(ActionEvent actionEvent) {
        switcher.screenSwitch("IllnessScreen.fxml", stage);
    }

    public void switchScreenToDiagnostic(ActionEvent actionEvent) {
        switcher.screenSwitch("DiagnosticScreen.fxml", stage);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
