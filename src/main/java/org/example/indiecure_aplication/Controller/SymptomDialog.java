package org.example.indiecure_aplication.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SymptomDialog implements Initializable {

    @FXML
    private TextField textField_symptomDialog_name;

    @FXML
    private TextArea textArea_symptomDialog_description;

    public String getTextField_symptomDialog_name() {
        return textField_symptomDialog_name.getText().toString();
    }

    public void setTextField_symptomDialog_name(String string) {
        this.textField_symptomDialog_name.setText(string);
    }

    public String getTextArea_symptomDialog_description() {
        return textArea_symptomDialog_description.getText().toString();
    }

    public void setTextArea_symptomDialog_description(String string) {
        this.textArea_symptomDialog_description.setText(string);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textField_symptomDialog_name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                String copy = textField_symptomDialog_name.getText().toString().substring(0, 30);
                textField_symptomDialog_name.setText(copy);
            }
        });
    }
}
