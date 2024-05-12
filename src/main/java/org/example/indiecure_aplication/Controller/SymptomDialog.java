package org.example.indiecure_aplication.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SymptomDialog {

    @FXML
    private TextField textField_symptomDialog_name;

    @FXML
    private TextArea textArea_symptomDialog_description;

    public TextField getTextField_symptomDialog_name() {
        return textField_symptomDialog_name;
    }

    public void setTextField_symptomDialog_name(String string) {
        this.textField_symptomDialog_name.setText(string);
    }

    public TextArea getTextArea_symptomDialog_description() {
        return textArea_symptomDialog_description;
    }

    public void setTextArea_symptomDialog_description(String string) {
        this.textArea_symptomDialog_description.setText(string);
    }
}
