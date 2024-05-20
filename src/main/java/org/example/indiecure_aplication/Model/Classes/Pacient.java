package org.example.indiecure_aplication.Model.Classes;

import java.sql.*;

public class Pacient extends IndieCureClass {
    int id;
    String name;
    int age;

    public Pacient() {

    }
    public Pacient(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    /*======================GETTERS AND SETTERS======================*/
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    /*============================================*/

    /*======================DATABASE CLASS MANAGEMENT======================*/
    /**
     * Inserts the class propierties into the table (pacient)
     */
    @Override
    public void addToDB(){
        try {
            assingId();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String statementInsert = "INSERT INTO pacient (id, name, age) VALUES (?, ?, ?)";
            PreparedStatement insert = connection.prepareStatement(statementInsert);
            insert.setInt(1, id);
            insert.setString(2, name);
            insert.setInt(3, age);
            insert.executeUpdate();
            insert.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("No se encontro la base de datos");
        }
    }

    /**
     * Modifies database entry which has same id as this class in the table (pacient)
     */
    @Override
    public void modifyOnDB(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String statementUpdate = "UPDATE pacient SET name = ?, age = ? WHERE id = ?";
            PreparedStatement update = connection.prepareStatement(statementUpdate);
            update.setString(1, name);
            update.setInt(2, age);
            update.setInt(3, id);
            update.executeUpdate();
            update.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("No se encontro la base de datos");
        }
    }

    /**
     * Removes from database the entry which has same id as this class in the table (pacient)
     */
    @Override
    public void removeFromDB(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String statementDelete = "DELETE FROM pacient WHERE id = ?";
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

    /**
     * checks if theres an entry on database which has same id as this class.
     * @return true if exist entry on database. false if doesnt exist entry on database
     */
    @Override
    public boolean checkIfExistInDB() {
        boolean exist = false;
        try  {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            String query = "SELECT name, age FROM pacient WHERE name = ?";
            PreparedStatement sentence = connection.prepareStatement(query);
            sentence.setString(1, name);
            ResultSet result = sentence.executeQuery();
            while (result.next()) {
                String pacientName = result.getString("name");
                int pacienteAge = result.getInt("age");
                if (pacientName.equalsIgnoreCase(name.toLowerCase())) {
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

    /**
     * auto-assing this id class based on the database existing ids
     */
    public void assingId(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");

            int illCount = 1;
            int illId = 0;
            Statement sentence = connection.createStatement();
            String query = "SELECT * FROM pacient";
            ResultSet result = sentence.executeQuery(query);
            while (result.next()) {
                int actualId = result.getInt("id");
                if (illId == 0 && actualId == illCount) {
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
    /*============================================*/
}