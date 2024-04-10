package org.example.indiecure;

import Classes.ScreenSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SessionLoginScreen {
    @FXML
    private TextField textFieldLoginName;
    @FXML
    private TextField textFieldLoginPassword;
    ScreenSwitcher screenSwitcher = new ScreenSwitcher();
    Stage stage;

    public void JoinApp(ActionEvent actionEvent) {
        if (textFieldLoginName.getText().toString().equals("Joseph") && textFieldLoginPassword.getText().toString().equals("1234")) {
            screenSwitcher.screenSwitch("HomeScreen.fxml", stage);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
