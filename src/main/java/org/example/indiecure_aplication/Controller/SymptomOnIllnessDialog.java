package org.example.indiecure_aplication.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.example.indiecure_aplication.Model.Classes.AlertDialog;
import org.example.indiecure_aplication.Model.Classes.Illness;
import org.example.indiecure_aplication.Model.Classes.Symptom;
import org.example.indiecure_aplication.Model.Exceptions.DuplicatedResultException;
import org.example.indiecure_aplication.Model.Utils.Checks;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;


public class SymptomOnIllnessDialog {
    @FXML
    Button btnAddSymptom, btnRemoveSymptom;
    @FXML
    Label labelSintoma;
    int testNumber = 1;
    ArrayList<TextField> symptomList = new ArrayList<>();
    AlertDialog alertDialog = new AlertDialog();
    @FXML
    private TextField textField_illnessDialog_symptom_1;
    @FXML
    private GridPane symptomOnIllnessDialogPane;
    Checks checks = new Checks();

    public void startDialog() {
        symptomList.add(textField_illnessDialog_symptom_1);
    }
    public void addSymptomOnIllness(ActionEvent actionEvent) {
        addField(null, actionEvent);
    }

    public void removeSymptomOnIllness(ActionEvent actionEvent) {
        removeField(actionEvent);
    }

    public void addField(String string, ActionEvent actionEvent){

        Label labelTest = new Label();
        TextField newTest = new TextField();
        Button btnAdd = new Button("+");
        Button btnRemove = new Button("-");

        labelTest.setTextFill(labelSintoma.getTextFill());
        labelTest.setFont(labelSintoma.getFont());
        btnAdd.setFont(btnAddSymptom.getFont());
        btnAdd.setTextFill(btnAddSymptom.getTextFill());
        btnAdd.setPrefSize(btnAddSymptom.getPrefWidth(), btnAddSymptom.getPrefHeight());
        btnAdd.setStyle("-fx-background-color:  #268C8A; -fx-background-radius:  50%;");
        btnRemove.setFont(btnRemoveSymptom.getFont());
        btnRemove.setTextFill(btnRemoveSymptom.getTextFill());
        btnRemove.setPrefSize(btnRemoveSymptom.getPrefWidth(), btnRemoveSymptom.getPrefHeight());
        btnRemove.setStyle("-fx-background-color:  #268C8A; -fx-background-radius:  50%;");

        testNumber++;

        if (string != null) {
            labelTest.setText("Sintoma" + testNumber);
            newTest.setId("textField_illnessDialog_symptom_" + testNumber);
            newTest.setText(string);
            btnAdd.setOnAction(event -> addField(null, event));
            btnRemove.setOnAction(event -> removeField(event));
        } else {
            labelTest.setText("Sintoma" + testNumber);
            newTest.setId("textField_illnessDialog_symptom_" + testNumber);
            btnAdd.setOnAction(event -> addField(null, event));
            btnRemove.setOnAction(event -> removeField(event));

        }

        int actualRow = symptomOnIllnessDialogPane.getRowCount();
        symptomOnIllnessDialogPane.add(labelTest, 0, actualRow);
        symptomOnIllnessDialogPane.add(newTest, 1, actualRow);
        symptomOnIllnessDialogPane.add(btnAdd, 2, actualRow);
        symptomOnIllnessDialogPane.add(btnRemove, 3, actualRow);
        symptomOnIllnessDialogPane.getRowConstraints().add(actualRow, symptomOnIllnessDialogPane.getRowConstraints().get(0));

        symptomList.add(newTest);
    }

    public void loadFields(Illness illness) {
        for (int i = 0;i < illness.getSymptomsList().size();i++) {
            if (i == 0) {
                textField_illnessDialog_symptom_1.setText(illness.getSymptomsList().get(i).getName());
            } else {
                ActionEvent actionEvent = new ActionEvent();
                addField(illness.getSymptomsList().get(i).getName(), actionEvent);
            }
        }
    }

    public void removeField(ActionEvent actionEvent){
        int rowIndex = symptomOnIllnessDialogPane.getRowIndex((Node) actionEvent.getSource());
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        if (symptomList.size() > 1) {
            testNumber--;
            for (Node node : symptomOnIllnessDialogPane.getChildren()) {
                Integer rowToCheck = symptomOnIllnessDialogPane.getRowIndex(node);
                if (rowToCheck != null && rowToCheck == rowIndex) {
                    if (node instanceof TextField) {
                        symptomList.remove(node);
                    }
                    nodeArrayList.add(node);
                }
            }
            symptomOnIllnessDialogPane.getChildren().removeAll(nodeArrayList);

            GridPane newGridPane = new GridPane();
            newGridPane.add(labelSintoma, 0, 0);
            newGridPane.add(textField_illnessDialog_symptom_1, 1, 0);
            newGridPane.add(btnAddSymptom, 2, 0);
            newGridPane.add(btnRemoveSymptom, 3, 0);


            ArrayList<ArrayList> rowLists = new ArrayList<>();
            for (int row = 1; row < symptomOnIllnessDialogPane.getRowCount(); row++) {
                for(Node node: symptomOnIllnessDialogPane.getChildren()){
                    if (!Objects.isNull(node) && symptomOnIllnessDialogPane.getRowIndex(node) == row && symptomOnIllnessDialogPane.getColumnIndex(node) == 0){
                        ArrayList<Node> helperArrayNodes = new ArrayList<>();
                        int col = 0;
                        for(Node nodehelper: symptomOnIllnessDialogPane.getChildren()){
                            if (symptomOnIllnessDialogPane.getRowIndex(nodehelper) == row && symptomOnIllnessDialogPane.getColumnIndex(nodehelper) == col){
                                helperArrayNodes.add(nodehelper);
                                col++;
                            }
                        }
                        rowLists.add(helperArrayNodes);
                    }
                }
            }
            for (ArrayList list: rowLists){
                int actualRow = newGridPane.getRowCount();
                newGridPane = addRow(newGridPane, list, actualRow);
            }
            System.out.println(newGridPane.getChildren());
            symptomOnIllnessDialogPane.getChildren().clear();
            symptomOnIllnessDialogPane.getChildren().setAll(newGridPane.getChildren());
            symptomOnIllnessDialogPane.getRowConstraints().remove(symptomOnIllnessDialogPane.getRowCount() - 1);
        }
    }

    public GridPane addRow (GridPane gridPane, ArrayList<Node> arrayList, int row) {
        int col = 0;
        for (Node nodeArray: arrayList){
            gridPane.add(nodeArray, col, row);
            col++;
        }
        return gridPane;
    }

    public ArrayList<Symptom> checkAndGetSymptoms(){
        ArrayList<Symptom> symptomArrayList = new ArrayList<>();
        try {
            if (!checks.checkIfRepeated(symptomList)){
                for (TextField textField: symptomList) {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
                    String query = "SELECT * FROM symptom WHERE name = ?";
                    PreparedStatement sentence = connection.prepareStatement(query);
                    sentence.setString(1, textField.getText().toString());
                    ResultSet result = sentence.executeQuery();
                    boolean duplicated = false;
                    while (result.next()) {
                        if (!duplicated) {
                            String nameSymptom = result.getString("name");
                            int idSymptom = result.getInt("id");
                            Symptom symptom = new Symptom(idSymptom, nameSymptom);
                            symptomArrayList.add(symptom);
                        }
                        else {
                            throw new DuplicatedResultException("Prueba medica duplicada");
                        }
                    }
                    result.close();
                    sentence.close();
                    connection.close();
                }
            } else {
                throw new DuplicatedResultException("hay campos repetidos en las pruebas medicas");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DuplicatedResultException dre) {
            symptomArrayList = new ArrayList<>();
            alertDialog.AlertError(dre.getMessage());
        }
        return symptomArrayList;
    }



    public ArrayList<TextField> getSymptomList() {
        return symptomList;
    }

    public void setSymptomList(ArrayList<TextField> symptomList) {
        this.symptomList = symptomList;
    }

    public TextField getTextField_illnessDialog_symptom_1() {
        return textField_illnessDialog_symptom_1;
    }

    public void setTextField_illnessDialog_symptom_1(TextField textField_illnessDialog_symptom_1) {
        this.textField_illnessDialog_symptom_1 = textField_illnessDialog_symptom_1;
    }
}
