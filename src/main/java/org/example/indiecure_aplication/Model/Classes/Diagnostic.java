package org.example.indiecure_aplication.Model.Classes;

import java.util.ArrayList;

public class Diagnostic extends IndieCureClass{
    int id;
    Pacient pacient;
    Illness illness;
    ArrayList<Symptom> symptomsList;


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
