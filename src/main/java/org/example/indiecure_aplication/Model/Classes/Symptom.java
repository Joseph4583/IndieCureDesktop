package org.example.indiecure_aplication.Model.Classes;

import java.sql.*;

public class Symptom extends IndieCureClass {
    int id;
    String name;
    String description;
    boolean isOfficial = true;

    public Symptom() {

    }
    public Symptom(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Symptom(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
            String statementInsert = "INSERT INTO symptom (id, name, description) VALUES (?, ?, ?)";
            PreparedStatement insert = connection.prepareStatement(statementInsert);
            insert.setInt(1, id);
            insert.setString(2, name);
            insert.setString(3, description);
            insert.executeUpdate();
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
            String statementUpdate = "UPDATE symptom SET name = ?, description = ? WHERE id = ?";
            PreparedStatement update = connection.prepareStatement(statementUpdate);
            update.setString(1, name);
            update.setString(2, description);
            update.setInt(3, id);
            update.executeUpdate();
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
            String statementDelete = "DELETE FROM symptom WHERE id = ?";
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
            String query = "SELECT name FROM symptom WHERE name = ?";
            PreparedStatement sentence = connection.prepareStatement(query);
            sentence.setString(1, name);
            ResultSet result = sentence.executeQuery();
            while (result.next()) {
                String symptomName = result.getString("name");
                if (symptomName.equals(name)) {
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

    public void assingId(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");

            int illCount = 1;
            int illId = 0;
            Statement sentence = connection.createStatement();
            String query = "SELECT * FROM symptom";
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
