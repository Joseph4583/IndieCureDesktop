package org.example.indiecure_aplication.Model.Classes;

import javafx.scene.control.Alert;

public class AlertDialog {
    public void AlertError(String string) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Ha surgido un error");
        alert.setContentText(string);
        alert.showAndWait();
    }

    public void AlertWarning(String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("WARNING");
        alert.setHeaderText("Advertencia");
        alert.setContentText(string);
        alert.showAndWait();
    }

    public void AlertInfo(String string) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFO");
        alert.setHeaderText("Informacion");
        alert.setContentText(string);
        alert.showAndWait();
    }
}
