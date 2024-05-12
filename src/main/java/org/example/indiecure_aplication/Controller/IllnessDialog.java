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
import org.example.indiecure_aplication.Model.Classes.AlertDialog;
import org.example.indiecure_aplication.Model.Classes.Illness;
import org.example.indiecure_aplication.Model.Classes.MedicalTest;
import org.example.indiecure_aplication.Model.Classes.Severity;
import org.example.indiecure_aplication.Model.Exceptions.DuplicatedResultException;
import org.example.indiecure_aplication.Model.Utils.Checks;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class IllnessDialog {

    @FXML
    private GridPane illnessDialogPane;
    @FXML
    private TextField textField_illnessDialog_name, textField_illnessDialog_test_1;
    @FXML
    private ComboBox comboBox_illnessDialog_severity;
    @FXML
    private Label label1, labelIllnessName, labelIllnessSeverity;
    @FXML
    private Button buttonAdd1, buttonRemove1;
    Severity severity;
    int testNumber = 1;
    ArrayList<TextField> testList = new ArrayList<>();
    AlertDialog alertDialog = new AlertDialog();
    Checks checks = new Checks();
    public void startDialog() {
        testList.add(textField_illnessDialog_test_1);

        ObservableList<Severity> boxOptions = FXCollections.observableArrayList();
        boxOptions.add(severity.Muy_leve);
        boxOptions.add(severity.Leve);
        boxOptions.add(severity.Moderado);
        boxOptions.add(severity.Grave);
        boxOptions.add(severity.Muy_Grave);
        boxOptions.add(severity.Letal);
        comboBox_illnessDialog_severity.setItems(boxOptions);
    }
    public void addTestOnIllness(ActionEvent actionEvent) {
        addField(null, actionEvent);
    }

    public void removeTestOnIllness(ActionEvent actionEvent) {
        removeField(actionEvent);
    }

    public void addField(String string, ActionEvent actionEvent){

        Label labelTest = new Label();
        TextField newTest = new TextField();
        Button btnAdd = new Button("+");
        Button btnRemove = new Button("-");
        testNumber++;

        labelTest.setTextFill(label1.getTextFill());
        labelTest.setFont(label1.getFont());
        btnAdd.setFont(buttonAdd1.getFont());
        btnAdd.setTextFill(buttonAdd1.getTextFill());
        btnAdd.setPrefSize(buttonAdd1.getPrefWidth(), buttonAdd1.getPrefHeight());
        btnAdd.setStyle("-fx-background-color:  #268C8A; -fx-background-radius:  50%;");
        btnRemove.setFont(buttonRemove1.getFont());
        btnRemove.setTextFill(buttonRemove1.getTextFill());
        btnRemove.setPrefSize(buttonRemove1.getPrefWidth(), buttonRemove1.getPrefHeight());
        btnRemove.setStyle("-fx-background-color:  #268C8A; -fx-background-radius:  50%;");


        if (string != null) {
            labelTest.setText("Prueba");
            newTest.setId("textField_illnessDialog_test_" + testNumber);
            newTest.setText(string);
            btnAdd.setOnAction(event -> addField(null, event));
            btnRemove.setId("buttonRemove" + testList);
            btnRemove.setOnAction(event -> removeField(event));
        } else {
            labelTest.setText("Prueba");
            newTest.setId("textField_illnessDialog_test_" + testNumber);
            btnAdd.setOnAction(event -> addField(null, event));
            btnRemove.setOnAction(event -> removeTestOnIllness(event));

        }

        int actualRow = illnessDialogPane.getRowCount();
        illnessDialogPane.add(labelTest, 0, actualRow);
        illnessDialogPane.add(newTest, 1, actualRow);
        illnessDialogPane.add(btnAdd, 2, actualRow);
        illnessDialogPane.add(btnRemove, 3, actualRow);
        illnessDialogPane.getRowConstraints().add(actualRow, illnessDialogPane.getRowConstraints().get(0));

        testList.add(newTest);

        System.out.println(illnessDialogPane.getRowCount());
    }

    public void loadFields(Illness illness) {
        comboBox_illnessDialog_severity.getSelectionModel().select(illness.getSeverity());
        textField_illnessDialog_name.setText(illness.getName());
        for (int i = 0;i < illness.getMedicalTestsList().size();i++) {
            if (i == 0) {
                textField_illnessDialog_test_1.setText(illness.getMedicalTestsList().get(i).getName());
            } else {
                ActionEvent actionEvent = null;
                addField(illness.getMedicalTestsList().get(i).getName(), actionEvent);
            }
        }
    }

    public void removeField (ActionEvent actionEvent) {
        int rowIndex = illnessDialogPane.getRowIndex((Node) actionEvent.getSource());
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        if (testList.size() > 1) {
            testNumber--;
            for (Node node : illnessDialogPane.getChildren()) {
               Integer rowToCheck = illnessDialogPane.getRowIndex(node);
               if (rowToCheck != null && rowToCheck == rowIndex) {
                   if (node instanceof TextField) {
                       testList.remove(node);
                   }
                   nodeArrayList.add(node);
               }
            }
            illnessDialogPane.getChildren().removeAll(nodeArrayList);

            GridPane newGridPane = new GridPane();
            newGridPane.add(labelIllnessName, 0, 0);
            newGridPane.add(textField_illnessDialog_name, 1, 0);
            newGridPane.add(labelIllnessSeverity, 0, 1);
            newGridPane.add(comboBox_illnessDialog_severity, 1, 1);
            newGridPane.add(label1, 0, 2);
            newGridPane.add(textField_illnessDialog_test_1, 1, 2);
            newGridPane.add(buttonAdd1, 2, 2);
            newGridPane.add(buttonRemove1, 3, 2);


            ArrayList<ArrayList> rowLists = new ArrayList<>();
            for (int row = 3; row < illnessDialogPane.getRowCount(); row++) {
                for(Node node: illnessDialogPane.getChildren()){
                    if (!Objects.isNull(node) && illnessDialogPane.getRowIndex(node) == row && illnessDialogPane.getColumnIndex(node) == 0){
                        ArrayList<Node> helperArrayNodes = new ArrayList<>();
                        int col = 0;
                        for(Node nodehelper: illnessDialogPane.getChildren()){
                            if (illnessDialogPane.getRowIndex(nodehelper) == row && illnessDialogPane.getColumnIndex(nodehelper) == col){
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
            illnessDialogPane.getChildren().clear();
            illnessDialogPane.getChildren().setAll(newGridPane.getChildren());
            illnessDialogPane.getRowConstraints().remove(illnessDialogPane.getRowCount() - 1);
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

    public ArrayList<MedicalTest> checkAndGetMedicalTests(){
        ArrayList<MedicalTest> testArrayList = new ArrayList<>();
        try {
            if (!checks.checkIfRepeated(testList)){
                for (TextField textField: testList) {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
                    String query = "SELECT * FROM medical_test WHERE name = ?";
                    PreparedStatement sentence = connection.prepareStatement(query);
                    sentence.setString(1, textField.getText().toString());
                    ResultSet result = sentence.executeQuery();
                    boolean duplicated = false;
                    while (result.next()) {
                        if (!duplicated) {
                            String nameTest = result.getString("name");
                            int idTest = result.getInt("id");
                            MedicalTest medicalTest = new MedicalTest(idTest, nameTest);
                            testArrayList.add(medicalTest);
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
            testArrayList = new ArrayList<>();
            alertDialog.AlertError(dre.getMessage());
        }
        return testArrayList;
    }

    public TextField getTextField_illnessDialog_name() {
        return textField_illnessDialog_name;
    }

    public void setTextField_illnessDialog_name(TextField textField_illnessDialog_name) {
        this.textField_illnessDialog_name = textField_illnessDialog_name;
    }

    public Severity getComboBox_illnessDialog_severity() {
        return (Severity) comboBox_illnessDialog_severity.getSelectionModel().getSelectedItem();
    }

    public void setComboBox_illnessDialog_severity(ComboBox comboBox_illnessDialog_severity) {
        this.comboBox_illnessDialog_severity = comboBox_illnessDialog_severity;
    }

    public TextField getTextField_illnessDialog_test_1() {
        return textField_illnessDialog_test_1;
    }

    public void setTextField_illnessDialog_test_1(TextField textField_illnessDialog_test_1) {
        this.textField_illnessDialog_test_1 = textField_illnessDialog_test_1;
    }

    public ArrayList<TextField> getTestList() {
        return testList;
    }

    public void setTestList(ArrayList<TextField> testList) {
        this.testList = testList;
    }

}
