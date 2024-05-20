package org.example.indiecure_aplication.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.indiecure_aplication.Model.Classes.Doctor;
import org.example.indiecure_aplication.Model.Classes.ScreenSwitcher;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class SessionLoginScreen implements Initializable {

    @FXML
    TextField textFieldLoginName, textFieldLoginPassword_Unmask;
    @FXML
    PasswordField textFieldLoginPassword_Mask;
    @FXML
    Button btnShowHide;
    ArrayList<Doctor> doctorArrayList = new ArrayList<>();
    ScreenSwitcher screenSwitcher = new ScreenSwitcher();
    Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public SessionLoginScreen() {
    }

    /**
     * This is the attached function of the log in button where it will chekc your credentail to log on the app
     * @param actionEvent
     */
    public void JoinApp(ActionEvent actionEvent) {
        //checks the field visibility to use the correct field to checks field for credentials
        if (textFieldLoginPassword_Mask.isVisible()) {
            checkOnDB(textFieldLoginName.getText().toString(), textFieldLoginPassword_Mask.getText().toString());
        } else {
            checkOnDB(textFieldLoginName.getText().toString(), textFieldLoginPassword_Unmask.getText().toString());
        }
    }

    /**
     * this method checks with the credentials on the database
     * @param name
     * @param password
     */
    public void checkOnDB(String name, String password) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb", "root", "");
            Statement sentence = connection.createStatement();

            //here we set the SQL sentence
            String query = "SELECT * FROM doctor";
            ResultSet result = sentence.executeQuery(query);
            doctorArrayList = new ArrayList<>();

            //cycles throw the database results and construct a new Doctor class
            while (result.next()) {
                if (name.equals(result.getString("name")) && password.equals(result.getString("password"))) {
                    Doctor doctorHelper = new Doctor();
                    doctorHelper.setId(result.getInt("id"));
                    doctorHelper.setName(result.getString("name"));
                    doctorHelper.setSpecializations(result.getString("specialization"));
                    screenSwitcher.screenSwitch("HomeScreen.fxml", stage, doctorHelper);
                }
            }
            // close resources
            result.close();
            sentence.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Unused
    public Stage getStage() {
        return stage;
    }

    /**
     * sets the stage for the screen
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * This is the attached function of the hide/show button. which switches the field btw a normal textfield and a passwordField
     * this way it creates the illusion of hidden and showing the password
     * @param actionEvent
     */
    public void showHideButton(ActionEvent actionEvent) {
        if (textFieldLoginPassword_Mask.isVisible()) {
            textFieldLoginPassword_Mask.setVisible(false);
            textFieldLoginPassword_Mask.setPrefSize(0, 0);
            textFieldLoginPassword_Unmask.setVisible(true);
            textFieldLoginPassword_Unmask.setPrefSize(350, 50);
            textFieldLoginPassword_Unmask.setText(textFieldLoginPassword_Mask.getText());
            btnShowHide.setText("Ocultar");
        } else {
            textFieldLoginPassword_Mask.setVisible(true);
            textFieldLoginPassword_Mask.setPrefSize(350, 50);
            textFieldLoginPassword_Unmask.setVisible(false);
            textFieldLoginPassword_Unmask.setPrefSize(0, 0);
            textFieldLoginPassword_Mask.setText(textFieldLoginPassword_Unmask.getText());
            btnShowHide.setText("Mostrar");
        }
    }
}
