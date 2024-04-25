package org.example.indiecure_aplication.Model.Classes;

public class Doctor {
    int id;
    String name;
    String specializations;

    public Doctor() {

    }

    public Doctor(int id, String name, String specializations) {
        this.id = id;
        this.name = name;
        this.specializations = specializations;
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

    public String getSpecializations() {
        return specializations;
    }

    public void setSpecializations(String specializations) {
        this.specializations = specializations;
    }
}
