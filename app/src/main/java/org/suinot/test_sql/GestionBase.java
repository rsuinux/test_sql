package org.suinot.test_sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by remi on 01/09/16.
 */

class GestionBase {


    private static final String TABLE_MEDIC = "table_medicaments";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NOM = "NOM";
    private static final int NUM_COL_NOM = 1;
    private static final String COL_DOSE = "DOSE";
    private static final int NUM_COL_DOSE = 2;

    private SQLiteDatabase bdd;
    private MabaseMedicament MaBase;


    public GestionBase(Context context) {
        //On crée la BDD et sa table
        Log.d ("GestionBase()", "création de la base");
        MaBase = new MabaseMedicament (context);
    }

    public void open() {
        //on ouvre la BDD en écriture
        Log.d ("open()", "ouverture de la base");
        bdd = MaBase.getWritableDatabase ();
    }

    public void close() {
        //on ferme l'accès à la BDD
        bdd.close ();
    }

    public SQLiteDatabase getBDD() {
        Log.d ("getBDD()", "renvoi la bdd");
        return bdd;
    }

    /**
     * Insère un médicament en base de données
     *
     * @param medic le médic à insérer
     * @return l'identifiant de la ligne insérée
     */
    public long insertMedicament(Medicament medic) {
        long i;
        Log.d ("insertMedic", "nom= " + medic.getMedicament ());
        Log.d ("insertMedic", "dose= " + medic.getDose ());
        // Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues ();
        // on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put (COL_NOM, medic.getMedicament ());
        values.put (COL_DOSE, medic.getDose ());

        i = bdd.insert (TABLE_MEDIC, null, values);
        Log.d ("insertMedic", "Retour de la base: " + i);
        return i;
    }

    /**
     * Met à jour le médicament en base de données
     *
     * @param id    l'identifiant du médicament à modifier
     * @param medic le nouveau médicament à associer à l'identifiant
     * @return le nombre de lignes modifiées
     */
    public long updateMedicament(int id, Medicament medic) {
        // à utiliser en cas d'appui court
        long i;
        ContentValues values = new ContentValues ();
        values.put (COL_NOM, medic.getMedicament ());
        values.put (COL_DOSE, medic.getDose ());
        i = bdd.update (TABLE_MEDIC, values, COL_ID + " = " + id, null);
        return i;
    }

    /**
     * Supprime un médicament de la BDD (celui dont l'identifiant est passé en
     * paramètres)
     *
     * @param id l'identifiant du medic
     * @return le nombre de medic supprimés
     */
    public long removeMedicamentWithID(long id) {
        //Suppression d'un médicament de la BDD grâce à l'ID avec un appui long
        long i;
        i = bdd.delete (TABLE_MEDIC, COL_ID + " = " + id, null);
        return i;
    }

    /**
     * Retourne le médicament dont le numéro de téléphone correspond à
     * celui en paramètre
     *
     * @param id l'identifiant du médic
     * @return le medic récupéré depuis la base de données
     */
    public Medicament getMedicamentWithId(int id) {
        //Récupère dans un Cursor les valeurs correspondants à un medicament contenu dans la BDD
        // (ici on sélectionne le medicament grâce à son id)
        String[] clauseSelect = new String[]{" * "};
        String clauseOu = COL_ID + " = ? ";
        String argsOu = Integer.toString (id);
        String orderBy = null;

        Cursor c = bdd.query (TABLE_MEDIC, clauseSelect, clauseOu, new String[]{argsOu}, null, null, orderBy);
        // Cursor c=bdd.query(TABLE_MEDIC, new String[]{COL_ID, COL_NOM, COL_DOSE }, null, null, COL_ID + " = " + id, null, null);
        return cursorToMedicament (c);
    }

    /**
     * Retourne le médicament dont le numéro de téléphone correspond à
     * celui en paramètre
     *
     * @param nom le nom du médic
     *            dose
     *            la dose du médic
     * @return le medic récupéré depuis la base de données
     */
    Medicament getMedicamentWithNom(String nom, String dose) {
        //Récupère dans un Cursor les valeurs correspondants à un medicament contenu dans la BDD
        // (ici on sélectionne le medicament grâce à son nom et sa dose)

        String[] clauseSelect = new String[]{" * "};
        String clauseOu = COL_NOM + " = ? AND " + COL_DOSE + " = ? ";
        String[] argsOu = new String[]{nom, dose};
        String orderBy = null;

        Cursor c = bdd.query (TABLE_MEDIC, clauseSelect, clauseOu, argsOu, null, null, orderBy);

        return cursorToMedicament (c);
    }

    /**
     * Convertit le cursor en médicament
     *
     * @param c le cursor à convertir
     * @return le médic créé à partir du Cursor
     */
    private Medicament cursorToMedicament(Cursor c) {
        //Cette méthode permet de convertir un cursor en un medicament
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount () == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst ();

        //On créé un medicament
        Medicament medic = new Medicament ();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        medic.setId (c.getInt (NUM_COL_ID));
        medic.setDose (c.getString (NUM_COL_DOSE));
        medic.setMedicament (c.getString (NUM_COL_NOM));
        //On ferme le cursor
        c.close ();

        //On retourne le médicament
        return medic;
    }

    // doit renvoyer le dernier element de la base
    long NombreMedicament() {
        String[] clauseSelect = new String[]{" * "};
        //String clauseOu = null;
        //String[] argsOu = null;
        //String orderBy = null;

        Cursor c = bdd.query (TABLE_MEDIC, clauseSelect, null, null, null, null, null);

        if (c.getCount () == 0)
            return 0;

        c.moveToLast ();
        //On créé un medicament
        Medicament medic = new Medicament ();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        medic.setId (c.getInt (NUM_COL_ID));
        medic.setDose (c.getString (NUM_COL_DOSE));
        medic.setMedicament (c.getString (NUM_COL_NOM));
        //On ferme le cursor
        c.close ();
        //On retourne le médicament
        return medic.getId ();
    }

    //retourne tous les médicaments de la bdd dans un arraylist pour afficher le listview
    ArrayList<ListItem> getAllMedicaments() {
        long i;
        Log.d ("ici getAll", "lecture base de donnée");
        ArrayList<ListItem> medicList = new ArrayList<> ();
        String[] clauseSelect = new String[]{" * "};
        //String clauseOu = null;
        //String[] argsOu = null;
        //String orderBy = null;
//        Cursor c = bdd.query (TABLE_MEDIC, clauseSelect, clauseOu, argsOu, null, null, orderBy);

        Cursor c = bdd.query (TABLE_MEDIC, clauseSelect, null, null, null, null, null);
        if (c.moveToFirst ()) {
            do {
                Log.d ("1 - getallmedic", c.getString (1));
                Log.d ("2 - getallmedic", c.getString (2));

                ListItem item1 = new ListItem (c.getString (1), c.getString (2));
                medicList.add (item1);
            } while (c.moveToNext ());
        }
        c.close ();
        return medicList;
    }

}