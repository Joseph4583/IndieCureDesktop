package org.example.indiecure_aplication.Model.Classes;

public class DiagnosticTransformed {

    int id;
    int idPacient;
    String namePacient;
    int idIllness;
    String nameIllness;
    String symptoms;

    public DiagnosticTransformed(int id, int idPacient, String namePacient, int idIllness, String nameIllness, String symptoms) {
        this.id = id;
        this.idPacient = idPacient;
        this.namePacient = namePacient;
        this.idIllness = idIllness;
        this.nameIllness = nameIllness;
        this.symptoms = symptoms;
    }

    public DiagnosticTransformed(Diagnostic diagnostic){
        this.id = diagnostic.getId();
        this.idPacient = diagnostic.getPacient().getId();
        this.namePacient = diagnostic.getPacient().getName();
        if (diagnostic.getIllness().getId() != 0) {
            this.idIllness = diagnostic.getIllness().getId();
            this.nameIllness = diagnostic.getIllness().getName();
        } else {
            this.idIllness = 0;
            this.nameIllness = "Sin registrar";
        }
        String str = "";
        for (Symptom symptom : diagnostic.getSymptomsList()) {
            str = str + symptom.getName() + "\n";
        }
        this.symptoms = str;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPacient() {
        return idPacient;
    }

    public void setIdPacient(int idPacient) {
        this.idPacient = idPacient;
    }

    public String getNamePacient() {
        return namePacient;
    }

    public void setNamePacient(String namePacient) {
        this.namePacient = namePacient;
    }

    public int getIdIllness() {
        return idIllness;
    }

    public void setIdIllness(int idIllness) {
        this.idIllness = idIllness;
    }

    public String getNameIllness() {
        return nameIllness;
    }

    public void setNameIllness(String nameIllness) {
        this.nameIllness = nameIllness;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}
