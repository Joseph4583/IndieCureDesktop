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
import org.example.indiecure_aplication.Model.Classes.ScreenSwitcher;
import org.example.indiecure_aplication.Model.Classes.Symptom;
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

public class SymptomScreen implements Initializable {
    Stage stage;
    Doctor doctor;
    ScreenSwitcher switcher = new ScreenSwitcher();
    AlertDialog alertDialog = new AlertDialog();
    Symptom symptom = new Symptom();
    ArrayList<Symptom> symptomArrayList = new ArrayList<>();
    @FXML
    private ListView<String> symptomListView;
    @FXML
    private Label labelName, labelDescription;
    ObservableList<String> items = FXCollections.observableArrayList();

    /**
     * Starts the ListView of Illnesses and assing the mouseClick Event to it
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
        refreshSymptomList();
        if (symptomArrayList.size() > 0) {
            for (Symptom symptomHelper : symptomArrayList) {
                if (symptomHelper.isOfficial()){
                    items.add(symptomHelper.getId() + " --- " + symptomHelper.getName() + " *");
                } else {
                    items.add(symptomHelper.getId() + " --- " + symptomHelper.getName());
                }
            }
            symptomListView.setItems(items);
        }
        symptomListView.setOnMouseClicked(event -> {
            String selectedItem = symptomListView.getSelectionModel().getSelectedItem();
            if (!Objects.isNull(selectedItem)) {
                for (Symptom symptomHelper: symptomArrayList){
                    if (symptomHelper.getId() == Integer.parseInt(selectedItem.split(" ")[0])) {
                        labelName.setText("Nombre: " + symptomHelper.getName());
                        labelDescription.setText(symptomHelper.getDescription());
                    }
                }
            }
        });
    }

    /*======================FXML CLICK EVENTS======================*/
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
     * this methods shows and process the content from SymptomDialog
     * @param mode
     */
    public void showDialogo(String mode) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("View/Dialogs/SymptomDialog.fxml"));
            DialogPane root = loader.load();
            SymptomDialog controller = loader.getController();

            // Configura la ventana emergente (diálogo)
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(root);
            if (mode.equals("add")) {
                dialog.setTitle("Añadir Sintoma");
            } else if (mode.equals("mod")) {
                dialog.setTitle("Modificar Sintoma");
                controller.setTextField_symptomDialog_name(symptom.getName());
                controller.setTextArea_symptomDialog_description(symptom.getDescription());
            }
            // Muestra el diálogo y espera a que el usuario interactúe con él
            boolean loopExit = false;
            while (!loopExit) {
                Optional<ButtonType> result  = dialog.showAndWait();
                if(result.get() == ButtonType.OK){
                    String nameField = controller.getTextField_symptomDialog_name().replaceAll("[^a-zA-Z\\s]", "").trim();
                    String descriptionField = controller.getTextArea_symptomDialog_description().trim();
                    if(!nameField.isEmpty() && !descriptionField.isBlank()){
                        symptom.setName(nameField);
                        symptom.setDescription(descriptionField);
                        loopExit = true;
                    } else {
                        alertDialog.AlertWarning("Algun campo esta vacio");
                    }
                } else if (result.get() == ButtonType.CANCEL) {
                    symptom = new Symptom();
                    loopExit = true;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * pops the Dialog and adds an symptom entry into the databse table (symptom).
     * @param actionEvent
     */
    public void addSymptom(ActionEvent actionEvent) {
        showDialogo("add");
        try {
            if (!Objects.isNull(symptom.getName())){
                if (!symptom.checkIfExistInDB()) {
                    symptom.addToDB();
                    refreshSymptomList();
                    items.clear();
                    for (Symptom symptomHelper : symptomArrayList) {
                        if (symptomHelper.isOfficial()){
                            items.add(symptomHelper.getId() + " --- " + symptomHelper.getName() + " *");
                        } else {
                            items.add(symptomHelper.getId() + " --- " + symptomHelper.getName());
                        }
                    }
                    symptomListView.setItems(items);
                } else {
                    throw new AlreadyExistingObject("El paciente ya existe");
                }
            } else {
                throw new CancelDialogException("Paciente cancelado");
            }

        } catch (AlreadyExistingObject aeo) {
            alertDialog.AlertError("El paciente ya existe");
        } catch (CancelDialogException cde) {

        }
    }

    /**
     * pops the Dialog and adds an symptom entry into the databse table (symptom).
     * @param actionEvent
     */
    public void modSymptom(ActionEvent actionEvent) {
        if (!symptomListView.getSelectionModel().isEmpty()) {
            String symptomStr = symptomListView.getSelectionModel().getSelectedItem();
            for (Symptom symptomHelper : symptomArrayList){
                if (symptomHelper.getId() == Integer.parseInt(symptomStr.split( " --- ")[0])) {
                    symptom = symptomHelper;

                }
            }
            System.out.println(symptom.getName() + symptom.isOfficial());
            if (!symptom.isOfficial()) {
                showDialogo("mod");
                try {
                    if (!Objects.isNull(symptom.getName())) {
                        symptom.modifyOnDB();
                        refreshSymptomList();
                        int index = symptomListView.getSelectionModel().getSelectedIndex();
                        items.set(index, symptom.getId() + " --- " + symptom.getName());
                    } else {
                        throw new CancelDialogException("Paciente cancelado");
                    }
                } catch (CancelDialogException cde) {

                }
            } else {
                alertDialog.AlertError("No se puede modificar sintomas oficiales");
            }

        } else {
            alertDialog.AlertWarning("No hay ningun paciente seleccionado");
        }
    }

    /**
     * pops the Dialog and removes symptom entry from the databse table (symptom)
     * @param actionEvent
     */
    public void removeSymptom(ActionEvent actionEvent) {
        if (!symptomListView.getSelectionModel().isEmpty()) {
            String symptomStr = symptomListView.getSelectionModel().getSelectedItem();
            for (Symptom symptomHelper : symptomArrayList){
                if (symptomHelper.getId() == Integer.parseInt(symptomStr.split( " --- ")[0])) {
                    symptom = symptomHelper;
                }
            }
            try {
                if (symptom.checkIfExistInDB()) {
                    System.out.println(symptom.getName() +  symptom.isOfficial());
                    if (!symptom.isOfficial()) {
                        int index = symptomListView.getSelectionModel().getSelectedIndex();
                        symptom.removeFromDB();
                        refreshSymptomList();
                        items.remove(index);
                    } else {
                        alertDialog.AlertError("No se puede borrar sintomas oficiales");
                    }
                } else {
                    throw new NonExistingObject("Este paciente no existe en la base de datos");
                }
            } catch (NonExistingObject neo) {
                alertDialog.AlertError("Este paciente no existe en la base de datos");
            }
        } else {
            alertDialog.AlertWarning("No hay ningun paciente seleccionado");
        }
    }

    /**
     * refresh the listView with database content of table (symptom)
     */
    public void refreshSymptomList() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb", "root", "");
            Statement sentence = connection.createStatement();
            String query = "SELECT * FROM symptom";
            ResultSet result = sentence.executeQuery(query);
            symptomArrayList = new ArrayList<>();
            while (result.next()) {
                Symptom symptomHelper = new Symptom();
                symptomHelper.setId(result.getInt("id"));
                symptomHelper.setName(result.getString("name"));
                symptomHelper.setDescription(result.getString("description"));
                symptomHelper.setOfficial(result.getBoolean("is_official"));
                symptomArrayList.add(symptomHelper);
            }
            // close resources
            result.close();
            sentence.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
