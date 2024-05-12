package org.example.indiecure_aplication.Model.Classes;

import java.sql.*;
import java.util.ArrayList;

public class Illness extends IndieCureClass {
    int id;
    String name;
    Severity severity;
    ArrayList<MedicalTest> medicalTestsList;
    ArrayList<Symptom> symptomsList;
    boolean isOfficial = true;

    public Illness() {

    }

    public Illness(int id, String name, Severity severity, ArrayList<Symptom> symptomsList, ArrayList<MedicalTest> medicalTestsList) {
        this.id = id;
        this.name = name;
        this.severity = severity;
        this.medicalTestsList = medicalTestsList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public ArrayList<MedicalTest> getMedicalTestsList() {
        return medicalTestsList;
    }

    public void setMedicalTestsList(ArrayList<MedicalTest> medicalTestsList) {
        this.medicalTestsList = medicalTestsList;
    }

    public ArrayList<Symptom> getSymptomsList() {
        return symptomsList;
    }

    public void setSymptomsList(ArrayList<Symptom> symptomsList) {
        this.symptomsList = symptomsList;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    @Override
    public void addToDB(){
        try {
            assingId();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String statementInsert = "INSERT INTO illness (id, name, severity) VALUES (?, ?, ?)";
            PreparedStatement insert = connection.prepareStatement(statementInsert);
            insert.setInt(1, id);
            insert.setString(2, name);
            insert.setString(3, severity.name());
            insert.executeUpdate();
            makeRelation("syn");
            makeRelation("test");
            insert.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("No se encontro la base de datos");
        }
    }

    @Override
    public void modifyOnDB(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String statementUpdate = "UPDATE illness SET name = ?, severity = ? WHERE id = ?";
            PreparedStatement update = connection.prepareStatement(statementUpdate);
            update.setString(1, name);
            update.setString(2, severity.name());
            update.setInt(3, id);
            update.executeUpdate();
            deleteRelation();
            makeRelation("syn");
            makeRelation("test");
            update.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("No se encontro la base de datos");
        }
    }

    @Override
    public void removeFromDB(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String statementDelete = "DELETE FROM illness WHERE id = ?";
            PreparedStatement delete = connection.prepareStatement(statementDelete);
            delete.setInt(1, id);
            delete.executeUpdate();
            delete.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("No se encontro la base de datos");
        }
    }

    @Override
    public boolean checkIfExistInDB() {
        boolean exist = false;
        try  {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String query = "SELECT name FROM illness WHERE name = ?";
            PreparedStatement sentence = connection.prepareStatement(query);
            sentence.setString(1, name);
            ResultSet result = sentence.executeQuery();
            while (result.next()) {
                String illnessName = result.getString("name");
                if (illnessName.equals(name)) {
                    exist = true;
                }
            }
            result.close();
            sentence.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return exist;
    }


    private void makeRelation(String mode){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");

            if (mode.equals("syn")) {
                String query = "INSERT INTO sym_ill (id_illness, id_symptom) VALUES (?, ?)";
                for (Symptom symptom: symptomsList){
                    PreparedStatement insert = connection.prepareStatement(query);
                    insert.setInt(1, id);
                    insert.setInt(2, symptom.getId());
                    insert.executeUpdate();
                    insert.close();
                }
            } else if (mode.equals("test")) {
                String query = "INSERT INTO ill_test (id_illness, id_medical_test) VALUES (?, ?)";
                for (MedicalTest medicalTest: medicalTestsList){
                    PreparedStatement insert = connection.prepareStatement(query);
                    insert.setInt(1, id);
                    insert.setInt(2, medicalTest.getId());
                    insert.executeUpdate();
                    insert.close();

                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("No se encontro la base de datos");
        }
    }

    private void deleteRelation(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String query = "DELETE FROM sym_ill WHERE id_illness = ?";
            PreparedStatement delete = connection.prepareStatement(query);
            delete.setInt(1, id);
            delete.executeUpdate();
            query = "DELETE FROM ill_test WHERE id_illness = ?";
            delete = connection.prepareStatement(query);
            delete.setInt(1, id);
            delete.executeUpdate();
            delete.close();

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("No se encontro la base de datos");
        }
    }

    public void findAndAssingRelations_symptoms() {
        symptomsList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb", "root", "");
            String query = "SELECT s.* " +
                    "FROM ((symptom s " +
                    "INNER JOIN sym_ill si ON s.id = si.id_symptom) " +
                    "INNER JOIN illness i ON si.id_illness = i.id) " +
                    "WHERE i.id = ?";
            PreparedStatement sentence = connection.prepareStatement(query);
            sentence.setInt(1, id);
            ResultSet result = sentence.executeQuery();
            while (result.next()) {
                Symptom symptom = new Symptom();
                symptom.setId(result.getInt("id"));
                symptom.setName(result.getString("name"));
                symptom.setDescription("description");
                symptomsList.add(symptom);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void findAndAssingRelations_medicalTest() {
        medicalTestsList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb", "root", "");
            String query = "SELECT mt.* " +
                    "FROM ((medical_test mt " +
                    "INNER JOIN ill_test it ON mt.id = it.id_medical_test) " +
                    "INNER JOIN illness i ON it.id_illness = i.id) " +
                    "WHERE i.id = ?";
            PreparedStatement sentence = connection.prepareStatement(query);
            sentence.setInt(1, id);
            ResultSet result = sentence.executeQuery();
            while (result.next()) {
                MedicalTest medicalTest = new MedicalTest();
                medicalTest.setId(result.getInt("id"));
                medicalTest.setName(result.getString("name"));
                medicalTestsList.add(medicalTest);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void assingId(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");

            int illCount = 1;
            int illId = 0;
            Statement sentence = connection.createStatement();
            String query = "SELECT * FROM illness";
            ResultSet result = sentence.executeQuery(query);
            while (result.next()) {
                if (result.getInt("id") != 0) {
                    if (illId == 0 && result.getInt("id") == illCount) {
                        illCount++;
                    } else {
                        illId = illCount;
                    }
                }
            }
            if (illId != 0) {
                id = illId;
            } else {
                id = illCount;
            }
            result.close();
            sentence.close();
            connection.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
