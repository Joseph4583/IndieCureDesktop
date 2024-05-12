package org.example.indiecure_aplication.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PacientDialog {
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
}
