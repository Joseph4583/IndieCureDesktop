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
import org.example.indiecure_aplication.Model.Classes.*;
import org.example.indiecure_aplication.Model.Exceptions.DuplicatedResultException;
import org.example.indiecure_aplication.Model.Utils.Checks;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * this is the Controller for the custom AlertDialog of DiagnosticScreen
 */
public class DiagnosticDialog implements Initializable {
    @FXML
    private ComboBox comboBox_diagnosticDialog_pacient, comboBox_diagnosticDialog_illness, comboBox_DiagnosticDialog_symptom_1;
    @FXML
    private GridPane diagnosisDialogPane;
    @FXML
    Button btnAddSymptomD, btnRemoveSymptomD;
    @FXML
    Label labelSintomaD, labelPacientD, labelIllnessD;
    int testNumber = 1;
    ArrayList<ComboBox> symptomsList = new ArrayList<>();
    ObservableList<String> boxOptionsS = FXCollections.observableArrayList();
    AlertDialog alertDialog = new AlertDialog();
    ArrayList<Pacient> pacientArrayList = new ArrayList<>();
    ArrayList<Illness> illnessArrayList = new ArrayList<>();
    Checks checks = new Checks();

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
        symptomsList.add(comboBox_DiagnosticDialog_symptom_1);
        ObservableList<String> boxOptionsP = loadPacientBox();
        comboBox_diagnosticDialog_pacient.setItems(boxOptionsP);
        comboBox_diagnosticDialog_pacient.getSelectionModel().select("0 --- ninguno");

        ObservableList<String> boxOptionsI = loadIllnessBox();
        comboBox_diagnosticDialog_illness.setItems(boxOptionsI);
        comboBox_diagnosticDialog_illness.getSelectionModel().select("0 --- ninguna");

        boxOptionsS = loadSymptomBox();
        comboBox_DiagnosticDialog_symptom_1.setItems(boxOptionsS);
        comboBox_DiagnosticDialog_symptom_1.getSelectionModel().select("0 --- ninguno");
    }

    /*======================FXML BUTTON ACTIONS======================*/
    public void addSymOnDiagnostic(ActionEvent actionEvent) {
        addField(null, actionEvent);
    }

    public void removeSymOnDiagnostic(ActionEvent actionEvent) {
        removeField(actionEvent);
    }
    /*============================================*/

    /*======================FIELD MANAGEMENT======================*/
    /**
     * Adds a new row with a label, combobox, button to field and button to remove field
     * This method is from Diagnostcis and adds symptoms to the DiagnosticScreen
     * @param string
     * @param actionEvent
     */
    public void addField(String string, ActionEvent actionEvent){

        Label labelTest = new Label();
        ComboBox newCombo = new ComboBox();
        Button btnAdd = new Button("+");
        Button btnRemove = new Button("-");

        //sets comboboxes + the styles and constraints onto the new row
        newCombo.setItems(boxOptionsS);
        newCombo.setStyle("-fx-background-color: #FFFFFF");
        newCombo.setPrefWidth(comboBox_DiagnosticDialog_symptom_1.getPrefWidth());
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

        //checks if parametter contains something. if doesnt. it selects the object on respective comboBox
        if (string != null) {
            labelTest.setText("Sintoma");
            newCombo.setId("comboBox_DiagnosticDialog_symptom_" + testNumber);
            newCombo.getSelectionModel().select(string);
            btnAdd.setOnAction(event -> addField(null, event));
            btnRemove.setId("buttonRemove" + symptomsList);
            btnRemove.setOnAction(event -> removeField(event));
        } else {
            labelTest.setText("Sintoma");
            newCombo.setId("comboBox_DiagnosticDialog_symptom_" + testNumber);
            newCombo.getSelectionModel().select("0 --- ninguno");
            btnAdd.setOnAction(event -> addField(null, event));
            btnRemove.setOnAction(event -> removeField(event));

        }

        //adds the nodes into the new row
        int actualRow = diagnosisDialogPane.getRowCount();
        diagnosisDialogPane.add(labelTest, 0, actualRow);
        diagnosisDialogPane.add(newCombo, 1, actualRow);
        diagnosisDialogPane.add(btnAdd, 2, actualRow);
        diagnosisDialogPane.add(btnRemove, 3, actualRow);
        diagnosisDialogPane.getRowConstraints().add(actualRow, diagnosisDialogPane.getRowConstraints().get(0));

        symptomsList.add(newCombo);
    }

    /**
     * Tranforms diagnostic class properties into the dialog content.
     * @param diagnostic
     */
    public void loadFields(Diagnostic diagnostic) {
        comboBox_diagnosticDialog_pacient.getSelectionModel().select(diagnostic.getPacient().getId() + " --- " + diagnostic.getPacient().getName());
        if (diagnostic.getId() != 0) {
            comboBox_diagnosticDialog_illness.getSelectionModel().select(diagnostic.getIllness().getId() + " --- " + diagnostic.getIllness().getName());
        }
        for (int i = 0;i < diagnostic.getSymptomsList().size();i++) {
            Symptom symptom = diagnostic.getSymptomsList().get(i);
            if (i == 0) {
                comboBox_DiagnosticDialog_symptom_1.getSelectionModel().select(symptom.getId() + " --- " +symptom.getName());
            } else {
                ActionEvent actionEvent = null;
                addField(symptom.getId() + " --- " +symptom.getName(), actionEvent);
            }
        }
    }

    /**
     * remove row from the dialog
     * @param actionEvent
     */
    public void removeField (ActionEvent actionEvent) {
        //this part checks the rowIndex from where the event comes and delete all the nodes locate in that row
        int rowIndex = diagnosisDialogPane.getRowIndex((Node) actionEvent.getSource());
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        if (symptomsList.size() > 1) {
            testNumber--;
            for (Node node : diagnosisDialogPane.getChildren()) {
                Integer rowToCheck = diagnosisDialogPane.getRowIndex(node);
                if (rowToCheck != null && rowToCheck == rowIndex) {
                    if (node instanceof ComboBox<?>) {
                        symptomsList.remove(node);
                    }
                    nodeArrayList.add(node);
                }
            }
            diagnosisDialogPane.getChildren().removeAll(nodeArrayList);

            //generating a now gridPane to replace the old
            GridPane newGridPane = new GridPane();
            newGridPane.add(labelPacientD, 0, 0);
            newGridPane.add(comboBox_diagnosticDialog_pacient, 1, 0);
            newGridPane.add(labelIllnessD, 0, 1);
            newGridPane.add(comboBox_diagnosticDialog_illness, 1, 1);
            newGridPane.add(labelSintomaD, 0, 2);
            newGridPane.add(comboBox_DiagnosticDialog_symptom_1, 1, 2);
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
     * loads the pacient comboBox with database content from table (pacient)
     * @return ObservableList<String>
     */
    private ObservableList<String> loadPacientBox(){
        ObservableList<String> boxOptions = FXCollections.observableArrayList();
        boxOptions.add("0 --- ninguno");
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
        return boxOptions;
    }

    /**
     * loads the illness comboBox with database content from table (illness)
     * @return ObservableList<String>
     */
    private ObservableList<String> loadIllnessBox(){
        ObservableList<String> boxOptions = FXCollections.observableArrayList();
        boxOptions.add("0 --- ninguna");
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
                    boxOptions.add(result.getInt("id") +  " --- " + result.getString("name"));
                }
            }
            result.close();
            sentence.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boxOptions;
    }

    /**
     * loads the symptom comboBox with database content from table (symptom)
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

    //Unused
    public ArrayList<ComboBox> getSymptomList() {
        return symptomsList;
    }

    /*======================EXTERNALIZE COMBOBOX CONTENT======================*/
    /**
     * Externalize comboBox selection as Pacient class
     * @return Pacient
     */
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

    /**
     * Externalize comboBox selection as Illness class
     * @return Illness
     */
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

    /**
     * Externalize comboBox selections as Symptom class list
     * @return ArrayList<Symptom>
     */
    public ArrayList<Symptom> getSymptomsFromComboBox() {
        ArrayList<Symptom> symptomArrayList = new ArrayList<>();
        try {
            if (!checks.checkIfRepeated(symptomsList)){
                for (ComboBox comboBox: symptomsList) {
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
    /*============================================*/
}
