package org.example.indiecure_aplication.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PacientDialog implements Initializable {
    @FXML
    private TextField textField_pacientDialog_name, textField_pacientDialog_age;

    public String getPacienteDialog_name() {
        return textField_pacientDialog_name.getText().toString();
    }

    public String getPacienteDialog_age() {
        return textField_pacientDialog_age.getText().toString();
    }

    public void setPacienteDialog_name(String string) {
        textField_pacientDialog_name.setText(string);
    }

    public void setPacienteDialog_age(String string) {
        textField_pacientDialog_age.setText(string);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textField_pacientDialog_name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                String copy = textField_pacientDialog_name.getText().toString().substring(0, 30);
                textField_pacientDialog_name.setText(copy);
            }
        });
        textField_pacientDialog_age.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 3) {
                String copy = textField_pacientDialog_age.getText().toString().substring(0, 3);
                textField_pacientDialog_age.setText(copy);
            }
        });
    }
}
