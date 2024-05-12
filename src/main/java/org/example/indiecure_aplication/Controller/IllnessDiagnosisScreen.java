package org.example.indiecure_aplication.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Model.Classes.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class IllnessDiagnosisScreen implements Initializable {
    Stage stage;
    Doctor doctor;
    ScreenSwitcher switcher = new ScreenSwitcher();
    @FXML
    private Label labelIllness1, labelIllness2, labelIllness3, labelIllness4, labelIllness5;
    @FXML
    private ProgressBar progressBar1, progressBar2, progressBar3, progressBar4, progressBar5;
    @FXML
    private Button buttonSendIllness1, buttonSendIllness2, buttonSendIllness3, buttonSendIllness4, buttonSendIllness5;
    @FXML
    private ListView<String> diagnosticListView;
    @FXML
    private Label labelSymptoms;
    ArrayList<Label> labelArrayList = new ArrayList<>();
    ArrayList<ProgressBar> progressBarArrayList = new ArrayList<>();
    ArrayList<Button> buttonArrayList = new ArrayList<>();
    Diagnostic diagnostic = new Diagnostic();
    ArrayList<Diagnostic> diagnosticArrayList = new ArrayList<>();
    ArrayList<Illness> illnessArrayList = new ArrayList<>();
    AlertDialog alertDialog = new AlertDialog();
    HashMap<Integer, Double> valueMap = new HashMap<>();
    ObservableList<String> items = FXCollections.observableArrayList();
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
        if (!doctor.getSpecializations().equals("Cabezera")) {
            switcher.screenSwitch("IllnessTestingScreen.fxml", stage, doctor);
        } else {
            alertDialog.AlertInfo("los medicos de cabezera no pueden entrar aqui");
        }
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
    }

    public void goToProfile(ActionEvent actionEvent) {
        switcher.screenSwitch("HomeScreen.fxml", stage, doctor);
    }

    public void closeSession(ActionEvent actionEvent) {
        switcher.LogOff(stage);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshDiagnosticList();
        refreshIllnessList();
        diagnosticListView.setItems(items);
        labelArrayList.add(labelIllness1);
        labelArrayList.add(labelIllness2);
        labelArrayList.add(labelIllness3);
        labelArrayList.add(labelIllness4);
        labelArrayList.add(labelIllness5);

        progressBarArrayList.add(progressBar1);
        progressBarArrayList.add(progressBar2);
        progressBarArrayList.add(progressBar3);
        progressBarArrayList.add(progressBar4);
        progressBarArrayList.add(progressBar5);

        buttonArrayList.add(buttonSendIllness1);
        buttonArrayList.add(buttonSendIllness2);
        buttonArrayList.add(buttonSendIllness3);
        buttonArrayList.add(buttonSendIllness4);
        buttonArrayList.add(buttonSendIllness5);

        for (int i = 0; i < labelArrayList.size(); i++) {
            labelArrayList.get(i).setVisible(false);
            progressBarArrayList.get(i).setVisible(false);
            buttonArrayList.get(i).setVisible(false);
        }

        diagnosticListView.setOnMouseClicked(event -> {
            String selectedItem = diagnosticListView.getSelectionModel().getSelectedItem();
            if(!Objects.isNull(selectedItem)) {
                for (Diagnostic diagnosticHelper : diagnosticArrayList){
                    if (diagnosticHelper.getId() == Integer.parseInt(selectedItem.split(" ")[0])) {
                        String strTest = "";
                        String strSymptom = "";
                        for (Symptom symptomHelper : diagnosticHelper.getSymptomsList()){
                            strSymptom = strSymptom + symptomHelper.getName() + "\n";
                        }
                        labelSymptoms.setText(strSymptom);

                        ArrayList<Illness> illnessArrayListHelper = new ArrayList<>();
                        for (Symptom symptomHelper : diagnosticHelper.getSymptomsList()){
                            for (Illness illnessHelper : illnessArrayList) {
                                for (Symptom symptomHelper2: illnessHelper.getSymptomsList()) {
                                    if(symptomHelper2.getName().equals(symptomHelper.getName())){
                                        illnessArrayListHelper.add(illnessHelper);
                                    }
                                }
                            }
                            illnessArrayList = illnessArrayListHelper;
                        }

                        double totalResult = 0;
                        valueMap.clear();
                        for (Illness illnessHelper: illnessArrayList) {
                            double result = (double) diagnosticHelper.getSymptomsList().size() / ((illnessHelper.getSymptomsList().size()  - diagnosticHelper.getSymptomsList().size()) + 1);
                            valueMap.put(illnessHelper.getId(), result);
                            totalResult += result;
                        }
                        for (Illness illnessHelper: illnessArrayList) {
                            double valuePercentage =  valueMap.get(illnessHelper.getId()) * 100 / totalResult;
                            valueMap.put(illnessHelper.getId(), valuePercentage);
                        }

                        illnessArrayList = sortProbabilities();

                        if (illnessArrayList.size() != 0) {
                            for (int i = 0; i < illnessArrayList.size(); i++) {
                                if (i <= 4) {
                                    labelArrayList.get(i).setVisible(true);
                                    labelArrayList.get(i).setText(illnessArrayList.get(i).getName());
                                    progressBarArrayList.get(i).setVisible(true);
                                    progressBarArrayList.get(i).setProgress(valueMap.get(illnessArrayList.get(i).getId()) / 100);
                                    buttonArrayList.get(i).setVisible(true);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void SendIllness1(ActionEvent actionEvent) {
        sendIllnessToDiagnostic(0);
    }

    public void SendIllness2(ActionEvent actionEvent) {
        sendIllnessToDiagnostic(1);
    }

    public void SendIllness3(ActionEvent actionEvent) {
        sendIllnessToDiagnostic(2);
    }

    public void SendIllness4(ActionEvent actionEvent) {
        sendIllnessToDiagnostic(3);
    }

    public void SendIllness5(ActionEvent actionEvent) {
        sendIllnessToDiagnostic(4);
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
                if (!diagnistocHelper.isConfirmed()) {
                    diagnosticArrayList.add(diagnistocHelper);
                    items.add(diagnistocHelper.getId() + " -- " + diagnistocHelper.getPacient().getName());
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

    public void refreshIllnessList() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb", "root", "");
            Statement sentence = connection.createStatement();
            String query = "SELECT * FROM illness";
            ResultSet result = sentence.executeQuery(query);
            illnessArrayList = new ArrayList<>();
            while (result.next()) {
                Illness illnessHelper = new Illness();
                if (result.getInt("id") != 0){
                    illnessHelper.setId(result.getInt("id"));
                    illnessHelper.setName(result.getString("name"));
                    illnessHelper.setSeverity(Severity.valueOf(result.getString("severity")));
                    illnessHelper.findAndAssingRelations_medicalTest();
                    illnessHelper.findAndAssingRelations_symptoms();
                    illnessArrayList.add(illnessHelper);
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

    private ArrayList<Illness> sortProbabilities(){

        ArrayList<Illness> sortedIllnessList = new ArrayList<>();
        ArrayList<Integer> sortAssistancelist = new ArrayList<>(valueMap.keySet());
        sortAssistancelist.sort(Comparator.comparingDouble(valueMap::get));
        Collections.reverse(sortAssistancelist);
        for (Integer intH : sortAssistancelist) {
            for (Illness illnessHelper: illnessArrayList) {
                if (illnessHelper.getId() == intH) {
                    sortedIllnessList.add(illnessHelper);
                }
            }
        }
        return  sortedIllnessList;
    };

    private void sendIllnessToDiagnostic(int index) {
        String selectedItem = diagnosticListView.getSelectionModel().getSelectedItem();
        if(!Objects.isNull(selectedItem)) {
            for (Diagnostic diagnosticHelper : diagnosticArrayList) {
                if (diagnosticHelper.getId() == Integer.parseInt(selectedItem.split(" ")[0])) {
                    diagnosticHelper.setIllness(illnessArrayList.get(index));
                    diagnosticHelper.modifyOnDB();
                }
            }
        }
    }
}


