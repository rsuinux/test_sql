package org.suinot.test_sql;

/**
 * Created by remi on 01/09/16.
 */
class Medicament {

    private int id;
    private String medicament;
    private String dosage;

    public Medicament() {
    }

    public Medicament(String nom, String dose) {
        super ();
        this.medicament = nom;
        this.dosage = dose;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicament() {
        return medicament;
    }

    public void setMedicament(String nom) {
        this.medicament = nom;
    }

    public String getDose() {
        return dosage;
    }

    public void setDose(String dose) {
        this.dosage = dose;
    }

    @Override
    public String toString() {
        return "[Medicament] id: " + id + "\nNom: " + medicament + "\ndose: " + dosage;
    }
}
