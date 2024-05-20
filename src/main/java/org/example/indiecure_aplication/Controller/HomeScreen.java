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

    /**
     * Starts the HomeScreen's clock and sets the ListView MouseEvent.
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
        ScheduledExecutorService delayset = Executors.newScheduledThreadPool(1);
        delayset.scheduleAtFixedRate(() -> {
            if (!Objects.isNull(doctor)) {
                labelDoctor.setText(doctor.getName() + " --- " + doctor.getSpecializations());
                refreshDiagnosticList();
                notificationListView.setItems(items);
                indicureThread.setLabel(labelTime);
                indicureThread.start();
                delayset.shutdown();
            }
        }, 0, 1000, TimeUnit.MICROSECONDS);

        notificationListView.setOnMouseClicked(event -> {
            String selectedItem = notificationListView.getSelectionModel().getSelectedItem();
            if(!Objects.isNull(selectedItem)) {
                for (Diagnostic diagnosticHelper : diagnosticArrayList){
                    if (diagnosticHelper.getId() == Integer.parseInt(selectedItem.split(" ")[0])) {
                        if (diagnosticHelper.getIllness().getId() == 0) {
                            tagGeneral.setText("Diagnostico sin enfermedad asignada");
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

    /*======================FXML CLICK EVENTS======================*/
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
    /*============================================*/

    /*======================GETTERS AND SETTERS======================*/
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /*============================================*/

    /*======================SCREEN FUNCIONALITY======================*/
    /**
     * refresh the listView with database content of table (diagnostic)
     */
    public void refreshDiagnosticList() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb", "root", "");
            Statement sentence = connection.createStatement();
            String query = "SELECT * FROM diagnostic";
            ResultSet result = sentence.executeQuery(query);
            diagnosticArrayList = new ArrayList<>();
            while (result.next()) {
                Diagnostic diagnosticHelper = new Diagnostic();
                diagnosticHelper.setId(result.getInt("id"));
                diagnosticHelper.findAndAssingPacient();
                diagnosticHelper.findAndAssingIllness();
                diagnosticHelper.findAndAssingSymptoms();
                diagnosticHelper.setConfirmed(result.getBoolean("is_confirmed"));
                switch (doctor.getSpecializations()) {
                    case "Admin":
                        diagnosticArrayList.add(diagnosticHelper);
                        items.add(diagnosticHelper.getId() + " --- " + diagnosticHelper.getPacient().getName());
                        break;
                    case "Cabezera":
                        if (diagnosticHelper.isConfirmed()) {
                            diagnosticArrayList.add(diagnosticHelper);
                            items.add(diagnosticHelper.getId() + " --- " + diagnosticHelper.getPacient().getName());
                        }
                        break;
                    case "Especial":
                        if (!diagnosticHelper.isConfirmed()) {
                            diagnosticArrayList.add(diagnosticHelper);
                            items.add(diagnosticHelper.getId() + " --- " + diagnosticHelper.getPacient().getName());
                        }
                        break;
                }
            }
            // close resource
            result.close();
            sentence.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*============================================*/
}
