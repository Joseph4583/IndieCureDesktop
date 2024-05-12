package org.example.indiecure_aplication.Model.Classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class Diagnostic extends IndieCureClass{
    int id;
    Pacient pacient;
    Illness illness;
    ArrayList<Symptom> symptomsList;
    boolean isConfirmed = false;
    ArrayList<Diagnostic> diagnosticArrayList;
    Diagnostic diagnostic;


    public Diagnostic() {

    }

    public Diagnostic(int id, Pacient pacient, ArrayList<Symptom> symptomsList) {
        this.id = id;
        this.pacient = pacient;
        this.symptomsList = symptomsList;
    }

    public Diagnostic(int id, Pacient pacient, Illness illness, ArrayList<Symptom> symptomsList) {
        this.id = id;
        this.pacient = pacient;
        this.illness = illness;
        this.symptomsList = symptomsList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pacient getPacient() {
        return pacient;
    }

    public void setPacient(Pacient pacient) {
        this.pacient = pacient;
    }

    public Illness getIllness() {
        return illness;
    }

    public void setIllness(Illness illness) {
        this.illness = illness;
    }

    public ArrayList<Symptom> getSymptomsList() {
        return symptomsList;
    }

    public void setSymptomsList(ArrayList<Symptom> symptomsList) {
        this.symptomsList = symptomsList;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    @Override
    public void addToDB(){
        try {
            assingId();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            if (!Objects.isNull(illness)){
                String statementInsert = "INSERT INTO diagnostic (id, id_pacient, id_illness) VALUES (?, ?, ?)";
                PreparedStatement insert = connection.prepareStatement(statementInsert);
                insert.setInt(1, id);
                insert.setInt(2, pacient.getId());
                insert.setInt(3, illness.getId());
                insert.executeUpdate();
                makeRelation();
                insert.close();
            } else {
                String statementInsert = "INSERT INTO diagnostic (id, id_pacient) VALUES (?, ?)";
                PreparedStatement insert = connection.prepareStatement(statementInsert);
                insert.setInt(1, id);
                insert.setInt(2, pacient.getId());
                insert.executeUpdate();
                makeRelation();
                insert.close();
            }
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
            String statementUpdate = "UPDATE diagnostic SET id_pacient = ?, id_illness = ?, is_confirmed = ? WHERE id = ?";
            PreparedStatement update = connection.prepareStatement(statementUpdate);
            update.setInt(1, pacient.getId());
            update.setInt(2, illness.getId());
            update.setBoolean(3, isConfirmed);
            update.setInt(4, id);
            update.executeUpdate();
            deleteRelation();
            makeRelation();
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
            String statementDelete = "DELETE FROM diagnostic WHERE id = ?";
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
            String query = "SELECT * FROM diagnostic WHERE id = ?";
            PreparedStatement sentence = connection.prepareStatement(query);
            sentence.setInt(1, id);
            ResultSet result = sentence.executeQuery();
            while (result.next()) {
                int diagnosticID = result.getInt("id");
                if (diagnosticID == id) {
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

    private void makeRelation(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String query = "INSERT INTO dia_sin (id_diagnostic, id_symptom) VALUES (?, ?)";
            for (Symptom symptom: symptomsList){
                PreparedStatement insert = connection.prepareStatement(query);
                insert.setInt(1, id);
                insert.setInt(2, symptom.getId());
                insert.executeUpdate();
                insert.close();
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
            String query = "DELETE FROM dia_sin WHERE id_diagnostic = ?";
            PreparedStatement delete = connection.prepareStatement(query);
            delete.setInt(1, id);
            delete.executeUpdate();
            delete.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("No se encontro la base de datos");
        }
    }

    public void findAndAssingPacient() {
        pacient = new Pacient();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb", "root", "");
            String query = "SELECT p.* FROM pacient p INNER JOIN diagnostic d ON p.id = d.id_pacient WHERE d.id = ?";
            PreparedStatement sentence = connection.prepareStatement(query);
            sentence.setInt(1, id);
            ResultSet result = sentence.executeQuery();
            while (result.next()) {
                pacient.setId(result.getInt("id"));
                pacient.setName(result.getString("name"));
                pacient.setAge(result.getInt("age"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void findAndAssingIllness() {
        illness = new Illness();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb", "root", "");
            String query = "SELECT i.* FROM illness i INNER JOIN diagnostic d ON i.id = d.id_illness WHERE d.id = ?";
            PreparedStatement sentence = connection.prepareStatement(query);
            sentence.setInt(1, id);
            ResultSet result = sentence.executeQuery();
            while (result.next()) {
                if (result.getInt("id") != 0) {
                    illness.setId(result.getInt("id"));
                    illness.setName(result.getString("name"));
                    illness.setSeverity(Severity.valueOf(result.getString("severity")));
                    illness.findAndAssingRelations_medicalTest();
                    illness.findAndAssingRelations_symptoms();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void findAndAssingSymptoms() {
        symptomsList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb", "root", "");
            String query = "SELECT s.* " +
                    "FROM ((symptom s " +
                    "INNER JOIN dia_sin di ON s.id = di.id_symptom) " +
                    "INNER JOIN diagnostic d ON di.id_diagnostic = d.id) " +
                    "WHERE d.id = ?";
            PreparedStatement sentence = connection.prepareStatement(query);
            sentence.setInt(1, id);
            ResultSet result = sentence.executeQuery();
            while (result.next()) {
                Symptom symptom = new Symptom();
                symptom.setId(result.getInt("id"));
                symptom.setName(result.getString("name"));
                symptom.setDescription(result.getString("description"));
                symptomsList.add(symptom);
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
            String query = "SELECT * FROM diagnostic";
            ResultSet result = sentence.executeQuery(query);
            while (result.next()) {
                if (illId == 0 && result.getInt("id") == illCount) {
                    illCount++;
                } else {
                    illId = illCount;
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
