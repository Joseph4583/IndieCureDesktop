package org.example.indiecure_aplication.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Model.Classes.*;
import org.example.indiecure_aplication.Model.Utils.IndicureThread;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class HomeScreen implements Initializable {
    Stage stage;
    Doctor doctor;
    ScreenSwitcher switcher = new ScreenSwitcher();
    @FXML
    private Label labelTime, labelDoctor, tagGeneral, tagTest, tageSynptom, labelSymptom, labelTest;
    @FXML
    private ListView<String> notificationListView;
    ObservableList<String> items = FXCollections.observableArrayList();
    ArrayList<Diagnostic> diagnosticArrayList = new ArrayList<>();
    AlertDialog alertDialog = new AlertDialog();
    IndicureThread indicureThread = new IndicureThread();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indicureThread.setLabel(labelTime);
        indicureThread.start();
        notificationListView.setItems(items);
        ScheduledExecutorService delayset = Executors.newScheduledThreadPool(1);
        delayset.scheduleAtFixedRate(() -> {
            if (!Objects.isNull(doctor)) {
                labelDoctor.setText(doctor.getName() + " --- " + doctor.getSpecializations());
                refreshDiagnosticList();
                delayset.shutdown();
            }
        }, 0, 1000, TimeUnit.MICROSECONDS);

        notificationListView.setOnMouseClicked(event -> {
            String selectedItem = notificationListView.getSelectionModel().getSelectedItem();
            if(!Objects.isNull(selectedItem)) {
                for (Diagnostic diagnosticHelper : diagnosticArrayList){
                    if (diagnosticHelper.getId() == Integer.parseInt(selectedItem.split(" ")[0])) {
                        if (diagnosticHelper.getIllness().getId() == 0) {
                            tagGeneral.setText("Diagnostico sin enfermedad asiganda");
                            String symptoms = "";
                            for (Symptom symptom: diagnosticHelper.getSymptomsList()) {
                                symptoms += symptom.getName() + "\n";
                            }
                            labelSymptom.setText(symptoms);
                            labelTest.setText("---");
                            tagTest.setText("---");
                            tageSynptom.setText("Sintomas");
                        } else if (diagnosticHelper.isConfirmed()) {
                            tagGeneral.setText("Diagnostico confirmado");
                            String symptoms = "";
                            for (Symptom symptom: diagnosticHelper.getSymptomsList()) {
                                symptoms += symptom.getName() + "\n";
                            }
                            tageSynptom.setText("Sintomas");
                            labelSymptom.setText(symptoms);
                            String medicalTests = "";
                            for (MedicalTest medicalTest: diagnosticHelper.getIllness().getMedicalTestsList()) {
                                medicalTests += medicalTest.getName() + "\n";
                            }
                            labelTest.setText(medicalTests);
                            tagTest.setText("Pruebas medicas");
                        } else if (!diagnosticHelper.isConfirmed()) {
                            tagGeneral.setText("Diagnostico sin confirmar");
                            String symptoms = "";
                            for (Symptom symptom: diagnosticHelper.getSymptomsList()) {
                                symptoms += symptom.getName() + "\n";
                            }
                            labelSymptom.setText(symptoms);
                            String medicalTests = "";
                            for (MedicalTest medicalTest: diagnosticHelper.getIllness().getMedicalTestsList()) {
                                medicalTests += medicalTest.getName() + "\n";
                            }
                            labelTest.setText(medicalTests);
                            tagTest.setText("Pruebas medicas");
                            tageSynptom.setText("Sintomas");
                        }
                    }
                }
            }
        });
    }

    public void switchScreenToHome(ActionEvent actionEvent) {

    }

    public void switchScreenToPacient(ActionEvent actionEvent) {
        indicureThread.setStop(true);
        switcher.screenSwitch("PacientScreen.fxml", stage, doctor);
    }

    public void switchScreenToIllnessDiagnosis(ActionEvent actionEvent) {
        if (!doctor.getSpecializations().equals("Especial")) {
            indicureThread.setStop(true);
            switcher.screenSwitch("IllnessDiagnosisScreen.fxml", stage, doctor);
        } else {
            alertDialog.AlertInfo("Los medico especialistas no pueden entrar aqui");
        }

    }

    public void switchScreenToIllnesstesting(ActionEvent actionEvent) {
        if (!doctor.getSpecializations().equals("Cabezera")) {
            indicureThread.setStop(true);
            switcher.screenSwitch("IllnessTestingScreen.fxml", stage, doctor);
        } else {
            alertDialog.AlertInfo("Los medicos de cabezera no pueden entrar aqui");
        }

    }
    public void switchScreenToSymptoms(ActionEvent actionEvent) {
        if (doctor.getSpecializations().equals("Admin")) {
            indicureThread.setStop(true);
            switcher.screenSwitch("SymptomScreen.fxml", stage, doctor);
        } else {
            alertDialog.AlertInfo("No tienes permisos de adminsitrador para acceder aqui");
        }

    }

    public void switchScreenToIllness(ActionEvent actionEvent) {
        if (doctor.getSpecializations().equals("Admin")) {
            indicureThread.setStop(true);
            switcher.screenSwitch("IllnessScreen.fxml", stage, doctor);
        } else {
            alertDialog.AlertInfo("No tienes permisos de adminsitrador para acceder aqui");
        }
    }

    public void switchScreenToDiagnostic(ActionEvent actionEvent) {
        if (!doctor.getSpecializations().equals("Especial")) {
            indicureThread.setStop(true);
            switcher.screenSwitch("DiagnosticScreen.fxml", stage, doctor);
        } else {
            alertDialog.AlertInfo("Los medico especialistas no pueden entrar aqui");
        }
    }

    public void goToProfile(ActionEvent actionEvent) {
        switcher.screenSwitch("HomeScreen.fxml", stage, doctor);
    }

    public void closeSession(ActionEvent actionEvent) {
        switcher.LogOff(stage);
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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
                diagnistocHelper.setConfirmed(result.getBoolean("is_confirmed"));
                switch (doctor.getSpecializations()) {
                    case "Admin":
                        diagnosticArrayList.add(diagnistocHelper);
                        items.add(diagnistocHelper.getId() + " --- " + diagnistocHelper.getPacient().getName());
                        break;
                    case "Cabezera":
                        if (diagnistocHelper.isConfirmed()) {
                            diagnosticArrayList.add(diagnistocHelper);
                            items.add(diagnistocHelper.getId() + " --- " + diagnistocHelper.getPacient().getName());
                        }
                        break;
                    case "Especial":
                        if (!diagnistocHelper.isConfirmed()) {
                            diagnosticArrayList.add(diagnistocHelper);
                            items.add(diagnistocHelper.getId() + " --- " + diagnistocHelper.getPacient().getName());
                        }
                        break;
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

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
