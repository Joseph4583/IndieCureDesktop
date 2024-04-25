package org.example.indiecure_aplication.Model.Classes;

public class Symptom extends IndieCureClass {
    int id;
    String name;
    String description;

    public Symptom() {

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

    @Override
    public void addToDB(){

    }

    @Override
    public void modifyOnDB(){

    }

    @Override
    public void removeFromDB(){

    }

    @Override
    public boolean checkIfExistInDB() {
        return false;
    }
}
