package org.example.indiecure_aplication.Model.Utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

/**
 * this class has some methods to check textfields and comboboxes conditions.
 */
public class Checks {

    /**
     * checks if 2 or more comboboxes has the selected item
     * @param comboBoxArrayList
     * @return true if theres a repeated selection. false if theres no repeated selection
     */
    public boolean checkIfRepeated(ArrayList<ComboBox> comboBoxArrayList){
        boolean repeat = false;
        for (ComboBox comboBoxA: comboBoxArrayList) {
            for (ComboBox comboBoxB: comboBoxArrayList) {
                if (!(comboBoxA.getId() == comboBoxB.getId())) {
                    if (comboBoxA.getSelectionModel().getSelectedItem().equals(comboBoxB.getSelectionModel().getSelectedItem())) {
                        repeat = true;
                    }
                }
            }
        }
        return repeat;
    }

    /**
     * checks if the a Textfield is empty or fill with spaces
     * @param array
     * @return true if theres a empty TextField. false if theres no empty TextField
     */
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
