package org.example.indiecure_aplication.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class IllnessDialog implements Initializable {

    @FXML
    private GridPane illnessDialogPane;
    @FXML
    private TextField textField_illnessDialog_name;
    @FXML
    private ComboBox comboBox_illnessDialog_severity, comboBox_illnessDialog_Test_1;
    @FXML
    private Label label1, labelIllnessName, labelIllnessSeverity;
    @FXML
    private Button buttonAdd1, buttonRemove1;
    Severity severity;
    int testNumber = 1;
    ArrayList<ComboBox> testList = new ArrayList<>();
    AlertDialog alertDialog = new AlertDialog();
    Checks checks = new Checks();
    ObservableList<String> boxMedicalOptions;

    /**
     * fills the dialog comboboxes with the respective tables info
     * and sets the textfield limit.
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
        testList.add(comboBox_illnessDialog_Test_1);
        boxMedicalOptions = loadMedicalTestBox();
        comboBox_illnessDialog_Test_1.setItems(boxMedicalOptions);
        comboBox_illnessDialog_Test_1.getSelectionModel().select("0 --- ninguna");

        ObservableList<Severity> boxOptions = FXCollections.observableArrayList();
        boxOptions.add(severity.Muy_leve);
        boxOptions.add(severity.Leve);
        boxOptions.add(severity.Moderado);
        boxOptions.add(severity.Grave);
        boxOptions.add(severity.Muy_Grave);
        boxOptions.add(severity.Letal);
        comboBox_illnessDialog_severity.setItems(boxOptions);
        comboBox_illnessDialog_severity.getSelectionModel().select(severity.Muy_leve);

        textField_illnessDialog_name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                String copy = textField_illnessDialog_name.getText().toString().substring(0, 30);
                textField_illnessDialog_name.setText(copy);
            }
        });
    }
    /*======================FXML BUTTON ACTIONS======================*/
    public void addTestOnIllness(ActionEvent actionEvent) {
        addField(null, actionEvent);
    }

    public void removeTestOnIllness(ActionEvent actionEvent) {
        removeField(actionEvent);
    }
    /*============================================*/

    /*======================FIELD MANAGEMENT======================*/
    /**
     * Adds a new row with a label, combobox, button to field and button to remove field
     * This method is from Illness and adds medical tests to the IllnessScreen
     * @param string
     * @param actionEvent
     */
    public void addField(String string, ActionEvent actionEvent){

        Label labelTest = new Label();
        ComboBox newCombo = new ComboBox();
        Button btnAdd = new Button("+");
        Button btnRemove = new Button("-");
        testNumber++;

        labelTest.setTextFill(label1.getTextFill());
        labelTest.setFont(label1.getFont());
        newCombo.setItems(boxMedicalOptions);
        newCombo.setStyle("-fx-background-color: #FFFFFF");
        newCombo.setPrefWidth(comboBox_illnessDialog_Test_1.getPrefWidth());
        btnAdd.setFont(buttonAdd1.getFont());
        btnAdd.setTextFill(buttonAdd1.getTextFill());
        btnAdd.setPrefSize(buttonAdd1.getPrefWidth(), buttonAdd1.getPrefHeight());
        btnAdd.setStyle("-fx-background-color: #268C8A; -fx-background-radius: 50%;");
        btnRemove.setFont(buttonRemove1.getFont());
        btnRemove.setTextFill(buttonRemove1.getTextFill());
        btnRemove.setPrefSize(buttonRemove1.getPrefWidth(), buttonRemove1.getPrefHeight());
        btnRemove.setStyle("-fx-background-color:  #268C8A; -fx-background-radius:  50%;");


        if (string != null) {
            labelTest.setText("Prueba");
            newCombo.setId("comboBox_illnessDialog_Test_" + testNumber);
            newCombo.getSelectionModel().select(string);
            btnAdd.setOnAction(event -> addField(null, event));
            btnRemove.setOnAction(event -> removeField(event));
        } else {
            labelTest.setText("Prueba");
            newCombo.setId("comboBox_illnessDialog_Test_" + testNumber);
            newCombo.getSelectionModel().select("0 --- ninguna");
            btnAdd.setOnAction(event -> addField(null, event));
            btnRemove.setOnAction(event -> removeTestOnIllness(event));

        }

        int actualRow = illnessDialogPane.getRowCount();
        illnessDialogPane.add(labelTest, 0, actualRow);
        illnessDialogPane.add(newCombo, 1, actualRow);
        illnessDialogPane.add(btnAdd, 2, actualRow);
        illnessDialogPane.add(btnRemove, 3, actualRow);
        illnessDialogPane.getRowConstraints().add(actualRow, illnessDialogPane.getRowConstraints().get(0));

        testList.add(newCombo);

        System.out.println(illnessDialogPane.getRowCount());
    }

    /**
     * Tranforms illness class properties into the dialog content.
     * @param illness
     */
    public void loadFields(Illness illness) {
        comboBox_illnessDialog_severity.getSelectionModel().select(illness.getSeverity());
        textField_illnessDialog_name.setText(illness.getName());
        for (int i = 0;i < illness.getMedicalTestsList().size();i++) {
            MedicalTest medicalTest = illness.getMedicalTestsList().get(i);
            if (i == 0) {
                comboBox_illnessDialog_Test_1.getSelectionModel().select(medicalTest.getId() + " --- " + medicalTest.getName());
            } else {
                ActionEvent actionEvent = null;
                addField(medicalTest.getId() + " --- " + medicalTest.getName(), actionEvent);
            }
        }
    }

    /**
     * remove row from the dialog
     * @param actionEvent
     */
    public void removeField (ActionEvent actionEvent) {
        int rowIndex = illnessDialogPane.getRowIndex((Node) actionEvent.getSource());
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        if (testList.size() > 1) {
            testNumber--;
            for (Node node : illnessDialogPane.getChildren()) {
               Integer rowToCheck = illnessDialogPane.getRowIndex(node);
               if (rowToCheck != null && rowToCheck == rowIndex) {
                   if (node instanceof ComboBox<?>) {
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
            newGridPane.add(comboBox_illnessDialog_Test_1, 1, 2);
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
    /*============================================*/

    /*======================COMBOBOX AND DIALOG CONTENT MANAGEMENT======================*/
    /**
     * adds the row to the gridPane.
     * @param gridPane
     * @param arrayList
     * @param row
     * @return
     */
    public GridPane addRow (GridPane gridPane, ArrayList<Node> arrayList, int row) {
        int col = 0;
        for (Node nodeArray: arrayList){
            gridPane.add(nodeArray, col, row);
            col++;
        }
        return gridPane;
    }

    /**
     * loads the medical tests comboBox with database content from table (medical_test)
     * @return ObservableList<String>
     */
    private ObservableList<String> loadMedicalTestBox() {
        ObservableList<String> boxOptions = FXCollections.observableArrayList();
        boxOptions.add("0 --- ninguna");
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String query = "SELECT * FROM medical_test";
            PreparedStatement sentence = connection.prepareStatement(query);
            ResultSet result = sentence.executeQuery();
            while (result.next()) {
                String nameTest = result.getString("name");
                int idTest = result.getInt("id");
                boxOptions.add(idTest + " --- " + nameTest);
            }
            result.close();
            sentence.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boxOptions;
    }
    /*============================================*/

    /*======================EXTERNALIZE COMBOBOX CONTENT======================*/
    /**
     * Externalize comboBox selection as an ArrayList of MedicalTest class
     * @return ArrayList<MedicalTest>
     */
    public ArrayList<MedicalTest> getTestsFromComboBox(){
        ArrayList<MedicalTest> testArrayList = new ArrayList<>();
        try {
            if (!checks.checkIfRepeated(testList)){
                for (ComboBox comboBox: testList) {
                    int id = Integer.parseInt(comboBox.getSelectionModel().getSelectedItem().toString().split(" ")[0]);
                    if (id != 0){
                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
                        String query = "SELECT * FROM medical_test WHERE id = ?";
                        PreparedStatement sentence = connection.prepareStatement(query);
                        sentence.setInt(1, id);
                        ResultSet result = sentence.executeQuery();
                        while (result.next()) {
                            String nameTest = result.getString("name");
                            int idTest = result.getInt("id");
                            MedicalTest medicalTest = new MedicalTest(idTest, nameTest);
                            testArrayList.add(medicalTest);
                        }
                        result.close();
                        sentence.close();
                        connection.close();
                    }
                }
            } else {
                throw new DuplicatedResultException("hay campos repetidos en las pruebas medicas");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DuplicatedResultException dre) {
            testArrayList = new ArrayList<>();
            alertDialog.AlertError("hay campos repetidos en las pruebas medicas");
        }
        return testArrayList;
    }

    /**
     * Externalize comboBox selection String of Illness class
     * @return String
     */
    public String getTextField_illnessDialog_name() {
        return textField_illnessDialog_name.getText().toString();
    }

    //Unused
    public void setTextField_illnessDialog_name(TextField textField_illnessDialog_name) {
        this.textField_illnessDialog_name = textField_illnessDialog_name;
    }

    /**
     * Externalize comboBox selection Severity of Illness class
     * @return Severity
     */
    public Severity getComboBox_illnessDialog_severity() {
        return (Severity) comboBox_illnessDialog_severity.getSelectionModel().getSelectedItem();
    }

    //Unused
    public void setComboBox_illnessDialog_severity(ComboBox comboBox_illnessDialog_severity) {
        this.comboBox_illnessDialog_severity = comboBox_illnessDialog_severity;
    }

    //Unused
    public ArrayList<ComboBox> getTestList() {
        return testList;
    }

    //Unused
    public void setTestList(ArrayList<ComboBox> testList) {
        this.testList = testList;
    }
    /*============================================*/
}
