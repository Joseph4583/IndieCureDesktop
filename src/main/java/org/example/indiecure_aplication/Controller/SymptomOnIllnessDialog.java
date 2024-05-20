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
import javafx.scene.layout.GridPane;
import org.example.indiecure_aplication.Model.Classes.AlertDialog;
import org.example.indiecure_aplication.Model.Classes.Illness;
import org.example.indiecure_aplication.Model.Classes.Symptom;
import org.example.indiecure_aplication.Model.Exceptions.DuplicatedResultException;
import org.example.indiecure_aplication.Model.Utils.Checks;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;


public class SymptomOnIllnessDialog implements Initializable {
    @FXML
    Button btnAddSymptom, btnRemoveSymptom;
    @FXML
    Label labelSintoma;
    int testNumber = 1;
    ArrayList<ComboBox> symptomList = new ArrayList<>();
    AlertDialog alertDialog = new AlertDialog();
    @FXML
    private ComboBox<String> comboBox_illnessDialog_symptom_1;
    @FXML
    private GridPane symptomOnIllnessDialogPane;
    Checks checks = new Checks();
    ObservableList<String> boxOptions = FXCollections.observableArrayList();

    /**
     * fills the dialog comboboxes with the respective tables info
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
        symptomList.add(comboBox_illnessDialog_symptom_1);
        boxOptions = loadSymptomBox();
        comboBox_illnessDialog_symptom_1.setItems(boxOptions);
        comboBox_illnessDialog_symptom_1.getSelectionModel().select("0 --- ninguno");
    }

    /*======================FXML BUTTON ACTIONS======================*/
    public void addSymptomOnIllness(ActionEvent actionEvent) {
        addField(null, actionEvent);
    }

    public void removeSymptomOnIllness(ActionEvent actionEvent) {
        removeField(actionEvent);
    }
    /*============================================*/

    /*======================FIELD MANAGEMENT======================*/
    /**
     * Adds a new row with a label, combobox, button to field and button to remove field
     * This method is from Illness and adds symptoms to the IllnessScreen
     * @param string
     * @param actionEvent
     */
    public void addField(String string, ActionEvent actionEvent){

        Label labelTest = new Label();
        ComboBox newCombo = new ComboBox();
        Button btnAdd = new Button("+");
        Button btnRemove = new Button("-");

        newCombo.setItems(boxOptions);
        newCombo.setStyle("-fx-background-color: #FFFFFF");
        newCombo.setPrefWidth(comboBox_illnessDialog_symptom_1.getPrefWidth());
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
            newCombo.setId("comboBox_illnessDialog_symptom_" + testNumber);
            newCombo.getSelectionModel().select(string);
            btnAdd.setOnAction(event -> addField(null, event));
            btnRemove.setOnAction(event -> removeField(event));
        } else {
            labelTest.setText("Sintoma" + testNumber);
            newCombo.setId("comboBox_illnessDialog_symptom_" + testNumber);
            newCombo.getSelectionModel().select("0 --- ninguno");
            btnAdd.setOnAction(event -> addField(null, event));
            btnRemove.setOnAction(event -> removeField(event));

        }

        int actualRow = symptomOnIllnessDialogPane.getRowCount();
        symptomOnIllnessDialogPane.add(labelTest, 0, actualRow);
        symptomOnIllnessDialogPane.add(newCombo, 1, actualRow);
        symptomOnIllnessDialogPane.add(btnAdd, 2, actualRow);
        symptomOnIllnessDialogPane.add(btnRemove, 3, actualRow);
        symptomOnIllnessDialogPane.getRowConstraints().add(actualRow, symptomOnIllnessDialogPane.getRowConstraints().get(0));

        symptomList.add(newCombo);
    }

    /**
     * Tranforms illness class properties into the dialog content.
     * @param illness
     */
    public void loadFields(Illness illness) {
        for (int i = 0;i < illness.getSymptomsList().size();i++) {
            if (i == 0) {
                Symptom symptom = illness.getSymptomsList().get(i);
                comboBox_illnessDialog_symptom_1.getSelectionModel().select(symptom.getId() + " --- " + symptom.getName());
            } else {
                ActionEvent actionEvent = new ActionEvent();
                Symptom symptom = illness.getSymptomsList().get(i);
                addField(symptom.getId() + " --- " + symptom.getName(), actionEvent);
            }
        }
    }

    /**
     * remove row from the dialog
     * @param actionEvent
     */
    public void removeField(ActionEvent actionEvent){
        int rowIndex = symptomOnIllnessDialogPane.getRowIndex((Node) actionEvent.getSource());
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        if (symptomList.size() > 1) {
            testNumber--;
            for (Node node : symptomOnIllnessDialogPane.getChildren()) {
                Integer rowToCheck = symptomOnIllnessDialogPane.getRowIndex(node);
                if (rowToCheck != null && rowToCheck == rowIndex) {
                    if (node instanceof ComboBox<?>) {
                        symptomList.remove(node);
                    }
                    nodeArrayList.add(node);
                }
            }
            symptomOnIllnessDialogPane.getChildren().removeAll(nodeArrayList);

            GridPane newGridPane = new GridPane();
            newGridPane.add(labelSintoma, 0, 0);
            newGridPane.add(comboBox_illnessDialog_symptom_1, 1, 0);
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
     * loads the symptoms comboBoxes with database content from table (symptoms)
     * @return ObservableList<String>
     */
    private ObservableList<String> loadSymptomBox() {
        ObservableList<String> boxOptions = FXCollections.observableArrayList();
        boxOptions.add("0 --- ninguno");
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String query = "SELECT * FROM symptom";
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
     * Externalize comboBox selection as an ArrayList of symptoms class
     * @return
     */
    public ArrayList<Symptom> getSymptomsFromComboBox(){
        ArrayList<Symptom> symptomArrayList = new ArrayList<>();
        try {
            if (!checks.checkIfRepeated(symptomList)){
                for (ComboBox comboBox: symptomList) {
                    int id = Integer.parseInt(comboBox.getSelectionModel().getSelectedItem().toString().split(" ")[0]);
                    if (id != 0 && comboBox.getSelectionModel().getSelectedItem() != null){
                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
                        String query = "SELECT * FROM symptom WHERE id = ?";
                        PreparedStatement sentence = connection.prepareStatement(query);
                        sentence.setInt(1, id);
                        ResultSet result = sentence.executeQuery();
                        while (result.next()) {
                            String nameTest = result.getString("name");
                            int idTest = result.getInt("id");
                            Symptom symptom = new Symptom(idTest, nameTest);
                            symptomArrayList.add(symptom);
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
            symptomArrayList = new ArrayList<>();
            alertDialog.AlertError("hay campos repetidos en las pruebas medicas");
        }
        return symptomArrayList;
    }

    //Unused
    public ArrayList<ComboBox> getSymptomList() {
        return symptomList;
    }

    //Unused
    public void setSymptomList(ArrayList<ComboBox> symptomList) {
        this.symptomList = symptomList;
    }
    /*============================================*/
}
