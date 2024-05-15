package org.example.indiecure_aplication.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Main;
import org.example.indiecure_aplication.Model.Classes.*;
import org.example.indiecure_aplication.Model.Exceptions.AlreadyExistingObject;
import org.example.indiecure_aplication.Model.Exceptions.CancelDialogException;
import org.example.indiecure_aplication.Model.Exceptions.NonExistingObject;
import org.example.indiecure_aplication.Model.Utils.Checks;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class IllnessScreen implements Initializable {
    Stage stage;
    Doctor doctor;
    ScreenSwitcher switcher = new ScreenSwitcher();
    Illness illness = new Illness();
    ArrayList<Illness> illnessArrayList = new ArrayList<>();
    AlertDialog alertDialog = new AlertDialog();
    Severity severity;
    @FXML
    private ListView<String> illnessListView;
    @FXML
    private Label labelName, labelSeverity, labelTest, labelSymptom;
    ObservableList<String> items = FXCollections.observableArrayList();
    Checks checks = new Checks();

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
    }

    public void switchScreenToDiagnostic(ActionEvent actionEvent) {
        if (!doctor.getSpecializations().equals("Especial")) {
            switcher.screenSwitch("DiagnosticScreen.fxml", stage, doctor);
        } else {
            alertDialog.AlertInfo("Los medicos especialistas no pueden entrar aqui");
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshIllnessList();
        if (illnessArrayList.size() > 0) {
            for (Illness illnessHelper : illnessArrayList) {
                items.add(illnessHelper.getId() + " --- " + illnessHelper.getName());
            }
            illnessListView.setItems(items);
        }

        illnessListView.setOnMouseClicked(event -> {
            String selectedItem = illnessListView.getSelectionModel().getSelectedItem();
            if (!Objects.isNull(selectedItem)) {
                for (Illness illnessHelper : illnessArrayList){
                    if (illnessHelper.getId() == Integer.parseInt(selectedItem.split(" ")[0])) {
                        labelName.setText("Nombre: " + illnessHelper.getName());
                        labelSeverity.setText("Gravedad: " + String.valueOf(illnessHelper.getSeverity()));
                        String strTest = "";
                        for (MedicalTest medicalTestHelper : illnessHelper.getMedicalTestsList()){
                            strTest = strTest + medicalTestHelper.getName() + "\n";
                        }
                        String strSymptom = "";
                        for (Symptom symptomHelper : illnessHelper.getSymptomsList()){
                            strSymptom = strSymptom + symptomHelper.getName() + "\n";
                        }
                        labelTest.setText(strTest);
                        labelSymptom.setText(strSymptom);
                    }
                }
            }
        });
    }

    public void showDialogo(String mode) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("View/Dialogs/IllnessDialog.fxml"));
            DialogPane root = loader.load();
            IllnessDialog controller = loader.getController();

            // Configura la ventana emergente (diálogo)
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(root);
            controller.startDialog();
            if (mode.equals("add")) {
                dialog.setTitle("Añadir Enfermedad");
            } else if (mode.equals("mod")) {
                dialog.setTitle("Modificar Enfermedad");
                controller.loadFields(illness);
            }
            // Muestra el diálogo y espera a que el usuario interactúe con él
            boolean loopExit = false;
            while (!loopExit) {
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Severity severity = controller.getComboBox_illnessDialog_severity();
                    String nameField = controller.getTextField_illnessDialog_name().replaceAll("[^a-zA-Z\\s]", "").trim();
                    ArrayList<TextField> testList = controller.getTestList();
                    if (!checks.checkIfEmpty(testList) && !nameField.isBlank()) {
                        illness.setName(nameField);
                        illness.setSeverity(severity);
                        ArrayList<MedicalTest> arraySymptomsList = controller.checkAndGetMedicalTests();
                        illness.setMedicalTestsList(arraySymptomsList);
                        showSymDialogo(mode);
                        loopExit = true;
                    } else {
                        alertDialog.AlertWarning("Algun campo esta vacio");
                    }
                } else if (result.get() == ButtonType.CANCEL) {
                    illness = new Illness();
                    loopExit = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSymDialogo(String mode) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("View/Dialogs/SymptomOnIllnessDialog.fxml"));
            DialogPane root = loader.load();
            SymptomOnIllnessDialog controller = loader.getController();

            // Configura la ventana emergente (diálogo)
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(root);
            controller.startDialog();
            if (mode.equals("add")) {
                dialog.setTitle("Añadir Sintomas a la enfermedad");
            } else if (mode.equals("mod")) {
                dialog.setTitle("Modificar Enfermedad");
                controller.loadFields(illness);
            }

            // Muestra el diálogo y espera a que el usuario interactúe con él
            boolean loopExit = false;
            while (!loopExit) {
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.get() == ButtonType.OK) {
                    ArrayList<TextField> symptomList = controller.getSymptomList();
                    if (!checks.checkIfEmpty(symptomList)) {
                        ArrayList<Symptom> arraySymptomsList = controller.checkAndGetSymptoms();
                        if (!Objects.isNull(arraySymptomsList)) {
                            illness.setSymptomsList(arraySymptomsList);
                            loopExit = true;
                        }
                    } else {
                        alertDialog.AlertWarning("Algun campo esta vacio");
                    }
                } else if (result.get() == ButtonType.CANCEL) {
                    illness = new Illness();
                    loopExit = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addIllness(ActionEvent actionEvent) {
        showDialogo("add");
        try {
            if (!Objects.isNull(illness.getName())) {
                if (!illness.checkIfExistInDB()) {
                    illness.addToDB();
                    refreshIllnessList();
                    items.clear();
                    for (Illness illnessHelper : illnessArrayList) {
                        items.add(illnessHelper.getId() + " --- " + illnessHelper.getName());
                    }
                    illnessListView.setItems(items);

                } else {
                    throw new AlreadyExistingObject("La enfermedad ya existe");
                }
            } else {
                throw new CancelDialogException("Enfermedad cancelada");
            }

        } catch (AlreadyExistingObject aeo) {
            alertDialog.AlertError(aeo.toString());
        } catch (CancelDialogException cde) {

        }
    }

    public void modIllness(ActionEvent actionEvent) {
        if (!illnessListView.getSelectionModel().isEmpty()) {
            String illnessStr = illnessListView.getSelectionModel().getSelectedItem();
            for (Illness illnessHelper : illnessArrayList){
                if (illnessHelper.getId() == Integer.parseInt(illnessStr.split(" --- ")[0])) {
                    illness = illnessHelper;
                }
            }
            if (!illness.isOfficial()){
                showDialogo("mod");
                try {
                    if (!Objects.isNull(illness.getName())) {
                        illness.modifyOnDB();
                        refreshIllnessList();
                        int index = illnessListView.getSelectionModel().getSelectedIndex();
                        items.set(index, illness.getId() + " --- " + illness.getName());
                    } else {
                        throw new CancelDialogException("Paciente cancelado");
                    }
                } catch (CancelDialogException cde) {

                }
            } else {
                alertDialog.AlertError("no se puede modificar enfermedades oficiales");
            }
        } else {
            alertDialog.AlertWarning("No hay ningun paciente seleccionado");
        }
    }

    public void removeIllness(ActionEvent actionEvent) {
        if (!illnessListView.getSelectionModel().isEmpty()) {
            String illnessStr = illnessListView.getSelectionModel().getSelectedItem();
            for (Illness illnessHelper : illnessArrayList){
                if (illnessHelper.getId() == Integer.parseInt(illnessStr.split(" --- ")[0])) {
                    illness = illnessHelper;
                }
            }
            try {
                if (illness.checkIfExistInDB()) {
                    if (!illness.isOfficial()) {
                        int index = illnessListView.getSelectionModel().getSelectedIndex();
                        illness.removeFromDB();
                        refreshIllnessList();
                        items.remove(index);
                    } else {
                        alertDialog.AlertError("No se puede borrar enfermedades oficiales");
                    }
                } else {
                    throw new NonExistingObject("Esta enfermedad no existe en la base de datos");
                }
            } catch (NonExistingObject neo) {
                alertDialog.AlertError(neo.toString());
            }
        } else {
            alertDialog.AlertWarning("No hay ninguna enfermedad seleccionada");
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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
                    illnessHelper.setOfficial(result.getBoolean("is_official"));
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
}
