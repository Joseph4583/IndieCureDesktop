package org.example.indiecure_aplication.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Model.Classes.Doctor;
import org.example.indiecure_aplication.Model.Classes.ScreenSwitcher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class SessionLoginScreen {

    @FXML
    TextField textFieldLoginName, textFieldLoginPassword;
    ArrayList<Doctor> doctorArrayList = new ArrayList<>();
    ScreenSwitcher screenSwitcher = new ScreenSwitcher();
    Stage stage;


    public SessionLoginScreen() throws NoSuchFieldException {
    }

    public void JoinApp(ActionEvent actionEvent) {
        checkOnDB(textFieldLoginName.getText().toString(), textFieldLoginPassword.getText().toString());
    }
    public void checkOnDB(String name, String password) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb", "root", "");
            Statement sentence = connection.createStatement();
            String query = "SELECT * FROM doctor";
            ResultSet result = sentence.executeQuery(query);
            doctorArrayList = new ArrayList<>();
            while (result.next()) {
                if (name.equals(result.getString("name")) && password.equals(result.getString("password"))) {
                    Doctor doctorHelper = new Doctor();
                    doctorHelper.setId(result.getInt("id"));
                    doctorHelper.setName(result.getString("name"));
                    doctorHelper.setSpecializations(result.getString("specialization"));
                    screenSwitcher.screenSwitch("HomeScreen.fxml", stage, doctorHelper);
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
