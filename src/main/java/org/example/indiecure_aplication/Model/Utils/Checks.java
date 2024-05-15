package org.example.indiecure_aplication.Model.Utils;

import javafx.scene.control.TextField;

import java.util.ArrayList;

public class Checks {

    public boolean checkIfRepeated(ArrayList<TextField> textFieldArrayList){
        boolean repeat = false;
        for (TextField textFieldA: textFieldArrayList) {
            for (TextField textFieldB: textFieldArrayList) {
                if (!(textFieldA.getId() == textFieldB.getId())) {
                    if (textFieldA.getText().toString().equals(textFieldB.getText().toString())) {
                        repeat = true;
                    }
                }
            }
        }
        return repeat;
    }

    public boolean checkIfEmpty(ArrayList<TextField> array){
        boolean empty = false;
        for (TextField textField : array) {
            if (textField.getText().isBlank()) {
                empty = true;
            }
        }
        return empty;
    }
}
