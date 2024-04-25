package org.example.indiecure_aplication.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Model.Classes.ScreenSwitcher;


public class SessionLoginScreen {

    @FXML
    TextField textFieldLoginName;
    @FXML
    TextField textFieldLoginPassword;
    ScreenSwitcher screenSwitcher = new ScreenSwitcher();
    Stage stage;


    public SessionLoginScreen() throws NoSuchFieldException {
    }

    public void JoinApp(ActionEvent actionEvent) {
        screenSwitcher.screenSwitch("HomeScreen.fxml", stage);
        //if (textFieldLoginName.getText().toString().equals("Joseph") && textFieldLoginPassword.getText().toString().equals("1234")) {}
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
