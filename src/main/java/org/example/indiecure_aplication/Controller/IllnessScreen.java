package org.example.indiecure_aplication.Controller;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Model.Classes.ScreenSwitcher;

public class IllnessScreen {
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
    public void switchScreenToSymptom(ActionEvent actionEvent) {
        switcher.screenSwitch("SymptomScreen.fxml", stage);
    }

    public void switchScreenToIllness(ActionEvent actionEvent) {
        switcher.screenSwitch("IllnessScreen.fxml", stage);
    }

    public void switchScreenToDiagnostic(ActionEvent actionEvent) {
        switcher.screenSwitch("DiagnosticScreen.fxml", stage);
    }

    public void addIllness(ActionEvent actionEvent) {
    }

    public void modIllness(ActionEvent actionEvent) {
    }

    public void removeIllness(ActionEvent actionEvent) {
    }
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void startIllnessList() {

    }
}
