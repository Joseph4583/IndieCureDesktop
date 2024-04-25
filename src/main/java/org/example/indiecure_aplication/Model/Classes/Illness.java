package org.example.indiecure_aplication.Model.Classes;

import java.util.ArrayList;

public class Illness extends IndieCureClass {
    int id;
    String name;
    Severity severity;
    ArrayList<MedicalTest> medicalTestsList;

    public Illness() {

    }

    public Illness(int id, String name, Severity severity, ArrayList<MedicalTest> medicalTestsList) {
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
