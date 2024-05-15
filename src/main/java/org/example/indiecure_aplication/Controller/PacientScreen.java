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
import org.example.indiecure_aplication.Model.Classes.AlertDialog;
import org.example.indiecure_aplication.Model.Classes.Doctor;
import org.example.indiecure_aplication.Model.Classes.Pacient;
import org.example.indiecure_aplication.Model.Classes.ScreenSwitcher;
import org.example.indiecure_aplication.Model.Exceptions.AlreadyExistingObject;
import org.example.indiecure_aplication.Model.Exceptions.CancelDialogException;
import org.example.indiecure_aplication.Model.Exceptions.NonExistingObject;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class PacientScreen implements Initializable {
    Pacient pacient = new Pacient();
    Doctor doctor;
    ArrayList<Pacient> pacientArrayList = new ArrayList<>();
    Stage stage;
    ScreenSwitcher switcher = new ScreenSwitcher();
    AlertDialog alertDialog = new AlertDialog();
    @FXML
    private ListView<String> pacientListView;
    @FXML
    private Label labelName, labelAge;
    ObservableList<String> items = FXCollections.observableArrayList();

    public void switchScreenToHome(ActionEvent actionEvent) {
        switcher.screenSwitch("HomeScreen.fxml", stage, doctor);
    }

    public void switchScreenToPacient(ActionEvent actionEvent) {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshPacientList();
        if (pacientArrayList.size() > 0) {
            for (Pacient pacientHelper : pacientArrayList) {
                items.add(pacientHelper.getId() + " --- " + pacientHelper.getName());
            }
            pacientListView.setItems(items);
        }



        pacientListView.setOnMouseClicked(event -> {
            String selectedItem = pacientListView.getSelectionModel().getSelectedItem();
            if (!Objects.isNull(selectedItem)) {
                for (Pacient pacientHelper: pacientArrayList){
                    if (pacientHelper.getId() == Integer.parseInt(selectedItem.split(" ")[0])) {
                        labelName.setText("Nombre: " + pacientHelper.getName());
                        labelAge.setText("Edad: " + pacientHelper.getAge());
                    }
                }
            }
        });
    }

    public void showDialogo(String mode) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("View/Dialogs/PacientDialog.fxml"));
            DialogPane root = loader.load();
            PacientDialog controller = loader.getController();

            // Configura la ventana emergente (diálogo)
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(root);
            if (mode.equals("add")) {
                dialog.setTitle("Añadir Paciente");
            } else if (mode.equals("mod")) {
                dialog.setTitle("Modificar Paciente");
                controller.setPacienteDialog_age(String.valueOf(pacient.getAge()));
                controller.setPacienteDialog_name(pacient.getName());
            }
            // Muestra el diálogo y espera a que el usuario interactúe con él
            boolean loopExit = false;
            while (!loopExit) {
                Optional<ButtonType> result  = dialog.showAndWait();
                if(result.get() == ButtonType.OK){
                    String nameField = controller.getPacienteDialog_name().replaceAll("[^a-zA-Z\\s]", "").trim();
                    int ageField = Integer.parseInt(controller.getPacienteDialog_age());
                    if(!nameField.isBlank() && !(ageField < 0) && !(ageField > 150)){
                        pacient.setName(nameField);
                        pacient.setAge(ageField);
                        loopExit = true;
                    } else {
                        alertDialog.AlertWarning("Algun campo esta vacio o la edad es incorrecta");
                    }
                } else if (result.get() == ButtonType.CANCEL) {
                    pacient = new Pacient();
                    loopExit = true;
                }
            }
        } catch (NumberFormatException nfe) {
            alertDialog.AlertWarning("la edad tiene que ser un numero y sin decimales");
            showDialogo(mode);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void addPacient(ActionEvent actionEvent) {
        showDialogo("add");
        try {
            if (!Objects.isNull(pacient.getName())){
                if (!pacient.checkIfExistInDB()) {
                    pacient.addToDB();
                    refreshPacientList();
                    items.clear();
                    for (Pacient pacientHelper : pacientArrayList) {
                        items.add(pacientHelper.getId() + " --- " + pacientHelper.getName());
                    }
                    pacientListView.setItems(items);
                } else {
                    throw new AlreadyExistingObject("El paciente ya existe");
                }
            } else {
                throw new CancelDialogException("Paciente cancelado");
            }

        } catch (AlreadyExistingObject aeo) {
            alertDialog.AlertError(aeo.toString());
        } catch (CancelDialogException cde) {

        }
    }
    public void removePacient(ActionEvent actionEvent) {
        if (!pacientListView.getSelectionModel().isEmpty()) {
            String pacientStr = pacientListView.getSelectionModel().getSelectedItem();
            for (Pacient pacienteHelper : pacientArrayList){
                if (pacienteHelper.getId() == Integer.parseInt(pacientStr.split(" ")[0])) {
                    pacient = pacienteHelper;
                }
            }
            try {
                if (pacient.checkIfExistInDB()) {
                    int index = pacientListView.getSelectionModel().getSelectedIndex();
                    pacient.removeFromDB();
                    refreshPacientList();
                    items.remove(index);
                } else {
                    throw new NonExistingObject("Este paciente no existe en la base de datos");
                }
            } catch (NonExistingObject neo) {
                alertDialog.AlertError(neo.toString());
            }
        } else {
            alertDialog.AlertWarning("No hay ningun paciente seleccionado");
        }
    }
    public void modPacient(ActionEvent actionEvent){
        if (!pacientListView.getSelectionModel().isEmpty()) {
            String pacientStr = pacientListView.getSelectionModel().getSelectedItem();
            for (Pacient pacienteHelper : pacientArrayList){
                if (pacienteHelper.getId() == Integer.parseInt(pacientStr.split(" ")[0])) {
                    pacient = pacienteHelper;

                }
            }
            showDialogo("mod");
            try {
                if (!Objects.isNull(pacient.getName())) {
                    pacient.modifyOnDB();
                    refreshPacientList();
                    int index = pacientListView.getSelectionModel().getSelectedIndex();
                    items.set(index, pacient.getId() + " --- " + pacient.getName());
                } else {
                    throw new CancelDialogException("Paciente cancelado");
                }
            } catch (CancelDialogException cde) {

            }
        } else {
            alertDialog.AlertWarning("No hay ningun paciente seleccionado");
        }
    }

    public void refreshPacientList() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb", "root", "");
            Statement sentence = connection.createStatement();
            String query = "SELECT * FROM pacient";
            ResultSet result = sentence.executeQuery(query);
            pacientArrayList = new ArrayList<>();
            while (result.next()) {
                Pacient pacientHelper = new Pacient();
                pacientHelper.setId(result.getInt("id"));
                pacientHelper.setName(result.getString("name"));
                pacientHelper.setAge(result.getInt("age"));
                pacientArrayList.add(pacientHelper);
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