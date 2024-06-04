package org.example.indiecure_aplication.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Model.Classes.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class IllnessTestingScreen implements Initializable {
    Stage stage;
    Doctor doctor;
    ScreenSwitcher switcher = new ScreenSwitcher();
    @FXML
    private GridPane gridPaneTest;
    @FXML
    private ListView<String> testingListView;
    @FXML
    private Label labelTest1;
    @FXML
    private RadioButton radioBtnPositive1, radioBtnNeutral1, radioBtnNegative1;
    ArrayList<RadioButton> radioButtonsPositive = new ArrayList<>(), radioButtonsNeutral = new ArrayList<>(), radioButtonsNegative = new ArrayList<>();
    ArrayList<Diagnostic> diagnosticArrayList = new ArrayList<>();
    AlertDialog alertDialog = new AlertDialog();
    ObservableList<String> items = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshDiagnosticList();
        testingListView.setItems(items);


        testingListView.setOnMouseClicked(event -> {
            String selectedItem = testingListView.getSelectionModel().getSelectedItem();
            int count = 1;
            gridPaneTest.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) != 0);
            while (gridPaneTest.getRowConstraints().size() > 1) {
                gridPaneTest.getRowConstraints().remove(1);
            }
            for (Diagnostic diagnostic: diagnosticArrayList) {
                System.out.println(Integer.parseInt(selectedItem.split(" ")[0]));
                if (diagnostic.getId() == Integer.parseInt(selectedItem.split(" ")[0])) {
                    for (MedicalTest medicalTest : diagnostic.getIllness().getMedicalTestsList()) {
                        addField(count, medicalTest);
                        count++;
                    }
                }
            }
        });



    }
    public void switchScreenToHome(ActionEvent actionEvent) {
        switcher.screenSwitch("HomeScreen.fxml", stage, doctor);
    }

    public void switchScreenToPacient(ActionEvent actionEvent) {
        switcher.screenSwitch("PacientScreen.fxml", stage, doctor);
    }

    public void switchScreenToIllnessDiagnosis(ActionEvent actionEvent) {
        if (!doctor.getSpecializations().equals("Especial")) {
            switcher.screenSwitch("IllnessDiagnosisScreen.fxml", stage, doctor);
        } else {
            alertDialog.AlertInfo("Los medicos especialistas no pueden entrar aqui");
        }
    }

    public void switchScreenToIllnesstesting(ActionEvent actionEvent) {
    }
    public void switchScreenToSymptom(ActionEvent actionEvent) {
        if (doctor.getSpecializations().equals("Admin")) {
            switcher.screenSwitch("SymptomScreen.fxml", stage, doctor);
        } else {
            alertDialog.AlertInfo("no tienes permisos de administrador");
        }
    }

    public void switchScreenToIllness(ActionEvent actionEvent) {
        if (doctor.getSpecializations().equals("Admin")) {
            switcher.screenSwitch("IllnessScreen.fxml", stage, doctor);
        } else {
            alertDialog.AlertInfo("no tienes permisos de administrador");
        }

    }

    public void switchScreenToDiagnostic(ActionEvent actionEvent) {
        if (!doctor.getSpecializations().equals("Especial")) {
            switcher.screenSwitch("DiagnosticScreen.fxml", stage, doctor);
        } else {
            alertDialog.AlertInfo("Los medicos especialistas no pueden entrar aqui");
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void goToProfile(ActionEvent actionEvent) {
        switcher.screenSwitch("HomeScreen.fxml", stage, doctor);
    }

    public void closeSession(ActionEvent actionEvent) {
        switcher.LogOff(stage);
    }

    public void erraseSelections(ActionEvent actionEvent) {
        clearSelections();
    }

    public void sendSelections(ActionEvent actionEvent) {
        if (!testingListView.getSelectionModel().isEmpty()) {
            String selectedItem = testingListView.getSelectionModel().getSelectedItem();
            for (Diagnostic diagnostic: diagnosticArrayList) {
                if (diagnostic.getId() == Integer.parseInt(selectedItem.split(" ")[0])) {
                    int count = 0;
                    boolean someEmptyPoint = false;
                    for (MedicalTest medicalTest : diagnostic.getIllness().getMedicalTestsList()) {
                        if (radioButtonsPositive.get(count).isSelected()) {
                            medicalTest.setTestResult(TestResult.Positive);
                        } else if (radioButtonsNeutral.get(count).isSelected()) {
                            medicalTest.setTestResult(TestResult.Not_conclusive);
                        } else if (radioButtonsNegative.get(count).isSelected()) {
                            medicalTest.setTestResult(TestResult.Negative);
                        } else {
                            someEmptyPoint = true;
                        }
                        count++;
                    }
                    if (someEmptyPoint){
                        alertDialog.AlertWarning("alguna prueba no fue marcada");
                    } else {
                        clearSelections();
                        diagnostic.setConfirmed(true);
                        diagnostic.modifyOnDB();
                        alertDialog.AlertInfo("El diagnostico fue confirmado");
                    }
                }
            }
        } else {
            alertDialog.AlertWarning("No hay ningun diagnostico seleccionado");
        }
    }

    private void clearSelections(){
        for (int i = 0; i < radioButtonsPositive.size(); i++) {
            radioButtonsPositive.get(i).setSelected(false);
            radioButtonsNeutral.get(i).setSelected(false);
            radioButtonsNegative.get(i).setSelected(false);
        }
    }

    public void refreshDiagnosticList() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb", "root", "");
            Statement sentence = connection.createStatement();
            String query = "SELECT * FROM diagnostic";
            ResultSet result = sentence.executeQuery(query);
            diagnosticArrayList = new ArrayList<>();
            while (result.next()) {
                Diagnostic diagnistocHelper = new Diagnostic();
                diagnistocHelper.setId(result.getInt("id"));
                diagnistocHelper.findAndAssingPacient();
                diagnistocHelper.findAndAssingIllness();
                diagnistocHelper.findAndAssingSymptoms();
                if (diagnistocHelper.getIllness().getId() != 0) {
                    diagnosticArrayList.add(diagnistocHelper);
                    items.add(diagnistocHelper.getId() + " --- " + diagnistocHelper.getPacient().getName());
                }

            }
            // Cierra los recursos
            result.close();
            sentence.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addField(int actualValue, MedicalTest medicalTest){

        Label labelTest = new Label();
        RadioButton newRadioPositive = new RadioButton(), newRadioNeutral = new RadioButton(), newRadioNegative = new RadioButton();

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.setUserData("toggle"+actualValue);
        newRadioPositive.setToggleGroup(toggleGroup);
        newRadioNeutral.setToggleGroup(toggleGroup);
        newRadioNegative.setToggleGroup(toggleGroup);
        newRadioPositive.setId("radioBtnPositive"+actualValue);
        newRadioNeutral.setId("radioBtnNeutral"+actualValue);
        newRadioNegative.setId("radioBtnNegative"+actualValue);

        radioButtonsPositive.add(newRadioPositive);
        radioButtonsNeutral.add(newRadioNeutral);
        radioButtonsNegative.add(newRadioNegative);

        labelTest.setTextFill(labelTest1.getTextFill());
        labelTest.setFont(labelTest1.getFont());
        labelTest.setText(medicalTest.getName());

        int actualRow = gridPaneTest.getRowCount();
        gridPaneTest.add(labelTest, 0, actualRow);
        gridPaneTest.add(newRadioPositive, 1, actualRow);
        gridPaneTest.add(newRadioNeutral, 2, actualRow);
        gridPaneTest.add(newRadioNegative, 3, actualRow);
        gridPaneTest.getRowConstraints().add(actualRow, gridPaneTest.getRowConstraints().get(0));

    }
}
