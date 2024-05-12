package org.example.indiecure_aplication.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Main;
import org.example.indiecure_aplication.Model.Classes.*;
import org.example.indiecure_aplication.Model.Exceptions.AlreadyExistingObject;
import org.example.indiecure_aplication.Model.Exceptions.CancelDialogException;
import org.example.indiecure_aplication.Model.Exceptions.NonExistingObject;
import org.example.indiecure_aplication.Model.Utils.Checks;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class DiagnosticScreen implements Initializable {
    Stage stage;
    Doctor doctor;
    ScreenSwitcher switcher = new ScreenSwitcher();
    Diagnostic diagnostic = new Diagnostic();
    ArrayList<Diagnostic> diagnosticArrayList = new ArrayList<>();
    AlertDialog alertDialog = new AlertDialog();
    @FXML
    private TableView<DiagnosticTransformed> table;
    @FXML
    private TableColumn<DiagnosticTransformed, Integer> id_diagnostico, id_paciente, id_enfermedad;
    @FXML
    private TableColumn<DiagnosticTransformed, String> nombre_paciente, nombre_enfermedad, sintomas;
    @FXML
    private RadioButton radioBtnPacient, radioBtnIllness, radioBtnSynptom;
    @FXML
    private TextField textFieldSearch;
    ObservableList<DiagnosticTransformed> items = FXCollections.observableArrayList();
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
    public void switchScreenToSymptoms(ActionEvent actionEvent) {
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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        id_diagnostico.setCellValueFactory(new PropertyValueFactory<DiagnosticTransformed, Integer>("id"));
        id_paciente.setCellValueFactory(new PropertyValueFactory<DiagnosticTransformed, Integer>("idPacient"));
        nombre_paciente.setCellValueFactory(new PropertyValueFactory<DiagnosticTransformed, String>("namePacient"));
        id_enfermedad.setCellValueFactory(new PropertyValueFactory<DiagnosticTransformed, Integer>("idIllness"));
        nombre_enfermedad.setCellValueFactory(new PropertyValueFactory<DiagnosticTransformed, String>("nameIllness"));
        sintomas.setCellValueFactory(new PropertyValueFactory<DiagnosticTransformed, String>("symptoms"));

        table.setItems(items);

        refreshDiagnosticList();
    }

    public void showDialogo(String mode) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("View/Dialogs/DiagnosticDialog.fxml"));
            DialogPane root = loader.load();
            DiagnosticDialog controller = loader.getController();

            // Configura la ventana emergente (diálogo)
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(root);
            controller.startDialog();
            if (mode.equals("add")) {
                dialog.setTitle("Añadir Sintomas a la enfermedad");
            } else if (mode.equals("mod")) {
                dialog.setTitle("Modificar Enfermedad");
                controller.loadFields(diagnostic);
            }


            // Muestra el diálogo y espera a que el usuario interactúe con él
            boolean loopExit = false;
            while (!loopExit) {
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Pacient pacientHelper = controller.getPacientFromComboBox();
                    if (pacientHelper.getId() != 0) {
                        Illness illnessHelper = controller.getIllnessFromComboBox();
                        ArrayList<TextField> symptomList = controller.getSymptomList();
                        if (!checks.checkIfEmpty(symptomList)) {
                            ArrayList<Symptom> arraySymptomsList = controller.checkAndGetSymptoms();
                            if (!Objects.isNull(arraySymptomsList)) {
                                diagnostic.setPacient(pacientHelper);
                                diagnostic.setSymptomsList(arraySymptomsList);
                                if (illnessHelper.getId() != 0){
                                    diagnostic.setIllness(illnessHelper);
                                }
                                loopExit = true;
                            }
                        } else {
                            alertDialog.AlertWarning("Algun campo esta vacio");
                        }
                    } else {
                        alertDialog.AlertWarning("No hay paciente asigando");
                    }
                } else if (result.get() == ButtonType.CANCEL) {
                    diagnostic = new Diagnostic();
                    loopExit = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDiagnostic(ActionEvent actionEvent) {
        showDialogo("add");
        try {
            if (diagnostic.getPacient().getId() != 0) {
                if (!diagnostic.checkIfExistInDB()) {
                    diagnostic.addToDB();
                    refreshDiagnosticList();
                    items.clear();
                    for (Diagnostic diagnosticHelper : diagnosticArrayList) {
                        items.add(new DiagnosticTransformed(diagnosticHelper));
                    }
                } else {
                    throw new AlreadyExistingObject("El diagnostico ya existe");
                }
            } else {
                throw new CancelDialogException("Enfermedad cancelada");
            }

        } catch (AlreadyExistingObject aeo) {
            alertDialog.AlertError(aeo.toString());
        } catch (CancelDialogException cde) {

        }
    }

    public void modDiagnostic(ActionEvent actionEvent) {
        if (!table.getSelectionModel().isEmpty()) {
            DiagnosticTransformed diagnosticSelected = table.getSelectionModel().getSelectedItem();
            for (Diagnostic diagnosticHelper : diagnosticArrayList){
                if (diagnosticHelper.getId() == diagnosticSelected.getId()) {
                    diagnostic = diagnosticHelper;

                }
            }
            showDialogo("mod");
            try {
                if (diagnostic.getId() != 0) {
                    diagnostic.modifyOnDB();
                    refreshDiagnosticList();
                    int index = table.getSelectionModel().getSelectedIndex();
                    items.set(index, new DiagnosticTransformed(diagnostic));
                } else {
                    throw new CancelDialogException("Paciente cancelado");
                }
            } catch (CancelDialogException cde) {

            }
        } else {
            alertDialog.AlertWarning("No hay ningun paciente seleccionado");
        }
    }

    public void removeDiagnostic(ActionEvent actionEvent) {
        if (!table.getSelectionModel().isEmpty()) {
            DiagnosticTransformed diagnosticSelected = table.getSelectionModel().getSelectedItem();
            for (Diagnostic diagnosticHelper : diagnosticArrayList){
                if (diagnosticHelper.getId() == diagnosticSelected.getId()) {
                    diagnostic = diagnosticHelper;
                }
            }
            try {
                if (diagnostic.checkIfExistInDB()) {
                    int index = table.getSelectionModel().getSelectedIndex();
                    diagnostic.removeFromDB();
                    refreshDiagnosticList();
                    items.remove(index);
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
                diagnosticArrayList.add(diagnistocHelper);
                items.add(new DiagnosticTransformed(diagnistocHelper));
            }
            // Cierra los recursos
            result.close();
            sentence.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchEntry(ActionEvent actionEvent) {
        if (textFieldSearch.getText().isEmpty()){
            table.setItems(items);
        } else {
            if (radioBtnPacient.isSelected()) {
                table.setItems(filterSearchList(textFieldSearch.getText().toString(), "pacient"));
            } else if (radioBtnSynptom.isSelected()) {
                table.setItems(filterSearchList(textFieldSearch.getText().toString(), "symptom"));
            } else if (radioBtnIllness.isSelected()) {
                table.setItems(filterSearchList(textFieldSearch.getText().toString(), "illness"));
            } else {
                alertDialog.AlertWarning("No hay ningun filtro seleccionado");
            }
        }
    }

    public ObservableList<DiagnosticTransformed> filterSearchList(String searchWord, String mode){
        ObservableList<DiagnosticTransformed> filtredItems = FXCollections.observableArrayList();
        for (DiagnosticTransformed diagnostic: items) {
            switch (mode){
                case "pacient":
                    if (diagnostic.getNamePacient().contains(searchWord)) {
                        filtredItems.add(diagnostic);
                    }
                    break;
                case "symptom":
                    if (diagnostic.getSymptoms().contains(searchWord)) {
                        filtredItems.add(diagnostic);
                    }
                    break;
                case "illness":
                    if (diagnostic.getNameIllness().contains(searchWord)) {
                        filtredItems.add(diagnostic);
                    }
                    break;
                default:
            }
        }
        if (filtredItems.size() == 0) {
            filtredItems.add(new DiagnosticTransformed(0,0,"Sin Coincidencias",0,"Sin Coincidencias","Sin Coincidencias"));
        }
        return filtredItems;
    }

    public void goToProfile(ActionEvent actionEvent) {
        switcher.screenSwitch("HomeScreen.fxml", stage, doctor);
    }

    public void closeSession(ActionEvent actionEvent) {
        switcher.LogOff(stage);
    }


}
