package org.example.indiecure_aplication.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.example.indiecure_aplication.Model.Classes.*;
import org.example.indiecure_aplication.Model.Exceptions.DuplicatedResultException;
import org.example.indiecure_aplication.Model.Utils.Checks;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class DiagnosticDialog {
    @FXML
    private ComboBox comboBox_diagnosticDialog_pacient, comboBox_diagnosticDialog_illness;
    @FXML
    private TextField textField_diagnosisDialog_symptom_1;
    @FXML
    private GridPane diagnosisDialogPane;
    @FXML
    Button btnAddSymptomD, btnRemoveSymptomD;
    @FXML
    Label labelSintomaD, labelPacientD, labelIllnessD;
    int testNumber = 1;
    ArrayList<TextField> simptomsList = new ArrayList<>();
    AlertDialog alertDialog = new AlertDialog();
    ArrayList<Pacient> pacientArrayList = new ArrayList<>();
    ArrayList<Illness> illnessArrayList = new ArrayList<>();
    Checks checks = new Checks();

    public void startDialog() {
        simptomsList.add(textField_diagnosisDialog_symptom_1);


        ObservableList<String> boxOptions = FXCollections.observableArrayList();
        try  {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String query = "SELECT * FROM pacient";
            PreparedStatement sentence = connection.prepareStatement(query);
            ResultSet result = sentence.executeQuery();
            while (result.next()) {
                Pacient pacientHelper = new Pacient();
                pacientHelper.setId(result.getInt("id"));
                pacientHelper.setName(result.getString("name"));
                pacientHelper.setAge(result.getInt("age"));
                pacientArrayList.add(pacientHelper);
                boxOptions.add(result.getInt("id") +  " --- " + result.getString("name"));
            }
            result.close();
            sentence.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        comboBox_diagnosticDialog_pacient.setItems(boxOptions);

        ObservableList<String> boxOptions2 = FXCollections.observableArrayList();
        try  {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String query = "SELECT * FROM illness";
            PreparedStatement sentence = connection.prepareStatement(query);
            ResultSet result = sentence.executeQuery();
            while (result.next()) {
                if (result.getInt("id") != 0) {
                    Illness illnessHelper = new Illness();
                    illnessHelper.setId(result.getInt("id"));
                    illnessHelper.setName(result.getString("name"));
                    illnessHelper.setSeverity(Severity.valueOf(result.getString("severity")));
                    illnessHelper.findAndAssingRelations_symptoms();
                    illnessHelper.findAndAssingRelations_medicalTest();
                    illnessArrayList.add(illnessHelper);
                    boxOptions2.add(result.getInt("id") +  " --- " + result.getString("name"));
                }
            }
            result.close();
            sentence.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        comboBox_diagnosticDialog_illness.setItems(boxOptions2);
    }
    public void addSymOnDiagnostic(ActionEvent actionEvent) {
        addField(null, actionEvent);
    }

    public void removeSymOnDiagnostic(ActionEvent actionEvent) {
        removeField(actionEvent);
    }

    public void addField(String string, ActionEvent actionEvent){

        Label labelTest = new Label();
        TextField newTest = new TextField();
        Button btnAdd = new Button("+");
        Button btnRemove = new Button("-");

        labelTest.setTextFill(labelSintomaD.getTextFill());
        labelTest.setFont(labelSintomaD.getFont());
        btnAdd.setFont(btnAddSymptomD.getFont());
        btnAdd.setTextFill(btnAddSymptomD.getTextFill());
        btnAdd.setPrefSize(btnAddSymptomD.getPrefWidth(), btnAddSymptomD.getPrefHeight());
        btnAdd.setStyle("-fx-background-color:  #268C8A; -fx-background-radius:  50%;");
        btnRemove.setFont(btnRemoveSymptomD.getFont());
        btnRemove.setTextFill(btnRemoveSymptomD.getTextFill());
        btnRemove.setPrefSize(btnRemoveSymptomD.getPrefWidth(), btnRemoveSymptomD.getPrefHeight());
        btnRemove.setStyle("-fx-background-color:  #268C8A; -fx-background-radius:  50%;");

        testNumber++;

        if (string != null) {
            labelTest.setText("Sintoma");
            newTest.setId("textField_diagnosisDialog_symptom_" + testNumber);
            newTest.setText(string);
            btnAdd.setOnAction(event -> addField(null, event));
            btnRemove.setId("buttonRemove" + simptomsList);
            btnRemove.setOnAction(event -> removeField(event));
        } else {
            labelTest.setText("Sintoma");
            newTest.setId("textField_diagnosisDialog_symptom_" + testNumber);
            btnAdd.setOnAction(event -> addField(null, event));
            btnRemove.setOnAction(event -> removeField(event));

        }

        int actualRow = diagnosisDialogPane.getRowCount();
        diagnosisDialogPane.add(labelTest, 0, actualRow);
        diagnosisDialogPane.add(newTest, 1, actualRow);
        diagnosisDialogPane.add(btnAdd, 2, actualRow);
        diagnosisDialogPane.add(btnRemove, 3, actualRow);
        diagnosisDialogPane.getRowConstraints().add(actualRow, diagnosisDialogPane.getRowConstraints().get(0));

        simptomsList.add(newTest);
    }

    public void loadFields(Diagnostic diagnostic) {
        comboBox_diagnosticDialog_pacient.getSelectionModel().select(diagnostic.getPacient().getId() + " --- " + diagnostic.getPacient().getName());
        if (diagnostic.getId() != 0) {
            comboBox_diagnosticDialog_illness.getSelectionModel().select(diagnostic.getIllness().getId() + " --- " + diagnostic.getIllness().getName());
        }
        for (int i = 0;i < diagnostic.getSymptomsList().size();i++) {
            if (i == 0) {
                textField_diagnosisDialog_symptom_1.setText(diagnostic.getSymptomsList().get(i).getName());
            } else {
                ActionEvent actionEvent = null;
                addField(diagnostic.getSymptomsList().get(i).getName(), actionEvent);
            }
        }
    }

    public void removeField (ActionEvent actionEvent) {
        System.out.println(diagnosisDialogPane.getRowIndex((Node) actionEvent.getSource()));
        int rowIndex = diagnosisDialogPane.getRowIndex((Node) actionEvent.getSource());
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        if (simptomsList.size() > 1) {
            testNumber--;
            for (Node node : diagnosisDialogPane.getChildren()) {
                Integer rowToCheck = diagnosisDialogPane.getRowIndex(node);
                if (rowToCheck != null && rowToCheck == rowIndex) {
                    if (node instanceof TextField) {
                        simptomsList.remove(node);
                    }
                    nodeArrayList.add(node);
                }
            }
            diagnosisDialogPane.getChildren().removeAll(nodeArrayList);

            GridPane newGridPane = new GridPane();
            newGridPane.add(labelPacientD, 0, 0);
            newGridPane.add(comboBox_diagnosticDialog_pacient, 1, 0);
            newGridPane.add(labelIllnessD, 0, 1);
            newGridPane.add(comboBox_diagnosticDialog_illness, 1, 1);
            newGridPane.add(labelSintomaD, 0, 2);
            newGridPane.add(textField_diagnosisDialog_symptom_1, 1, 2);
            newGridPane.add(btnAddSymptomD, 2, 2);
            newGridPane.add(btnRemoveSymptomD, 3, 2);

            ArrayList<ArrayList> rowLists = new ArrayList<>();
            for (int row = 3; row < diagnosisDialogPane.getRowCount(); row++) {
                for(Node node: diagnosisDialogPane.getChildren()){
                    if (!Objects.isNull(node) && diagnosisDialogPane.getRowIndex(node) == row && diagnosisDialogPane.getColumnIndex(node) == 0){
                        ArrayList<Node> helperArrayNodes = new ArrayList<>();
                        int col = 0;
                        for(Node nodehelper: diagnosisDialogPane.getChildren()){
                            if (diagnosisDialogPane.getRowIndex(nodehelper) == row && diagnosisDialogPane.getColumnIndex(nodehelper) == col){
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
            diagnosisDialogPane.getChildren().clear();
            diagnosisDialogPane.getChildren().setAll(newGridPane.getChildren());
            diagnosisDialogPane.getRowConstraints().remove(diagnosisDialogPane.getRowCount() - 1);
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
            if (!checks.checkIfRepeated(simptomsList)){
                for (TextField textField: simptomsList) {
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
        return simptomsList;
    }

    public Pacient getPacientFromComboBox() {
        Pacient pacientHelper = new Pacient();
        if (!comboBox_diagnosticDialog_pacient.getSelectionModel().isEmpty()) {
            String findId = (String) comboBox_diagnosticDialog_pacient.getSelectionModel().getSelectedItem();
            try  {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
                String query = "SELECT * FROM pacient WHERE id = ?";
                PreparedStatement sentence = connection.prepareStatement(query);
                sentence.setInt(1, Integer.parseInt(findId.split(" ")[0]));
                ResultSet result = sentence.executeQuery();
                while (result.next()) {
                    pacientHelper.setId(result.getInt("id"));
                    pacientHelper.setName(result.getString("name"));
                    pacientHelper.setAge(result.getInt("age"));
                }
                result.close();
                sentence.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return pacientHelper;
    }

    public Illness getIllnessFromComboBox() {
        Illness illnessHelper = new Illness();
        if (!comboBox_diagnosticDialog_illness.getSelectionModel().isEmpty()) {
            String findId = (String) comboBox_diagnosticDialog_illness.getSelectionModel().getSelectedItem();
            try  {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
                String query = "SELECT * FROM illness WHERE id = ?";
                PreparedStatement sentence = connection.prepareStatement(query);
                sentence.setInt(1, Integer.parseInt(findId.split(" ")[0]));
                ResultSet result = sentence.executeQuery();
                while (result.next()) {
                    illnessHelper.setId(result.getInt("id"));
                    illnessHelper.setName(result.getString("name"));
                    illnessHelper.setSeverity(Severity.valueOf(result.getString("severity")));
                    illnessHelper.findAndAssingRelations_symptoms();
                    illnessHelper.findAndAssingRelations_medicalTest();
                }
                result.close();
                sentence.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return illnessHelper;
    }
}
