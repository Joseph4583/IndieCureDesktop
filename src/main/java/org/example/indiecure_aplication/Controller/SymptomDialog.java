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

    /*======================GETTERS AND SETTERS======================*/

    /**
     * Externalize Textfield as a String
     * @return String
     */
    public String getTextField_symptomDialog_name() {
        return textField_symptomDialog_name.getText().toString();
    }

    /**
     * Externalize Textfield as a String
     * @return String
     */
    public void setTextField_symptomDialog_name(String string) {
        this.textField_symptomDialog_name.setText(string);
    }

    /**
     * sets textfield content
     * @param string
     */
    public String getTextArea_symptomDialog_description() {
        return textArea_symptomDialog_description.getText().toString();
    }

    /**
     * sets textfield content
     * @param string
     */
    public void setTextArea_symptomDialog_description(String string) {
        this.textArea_symptomDialog_description.setText(string);
    }
    /*============================================*/

    /**
     * sets the TextField char limit
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
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
