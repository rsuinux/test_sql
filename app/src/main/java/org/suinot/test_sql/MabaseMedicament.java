package org.suinot.test_sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by remi on 01/09/16.
 */

class MabaseMedicament extends SQLiteOpenHelper {

    private String DATABASE_PATH; // chemin défini dans le constructeur
    static final String NOM_BDD = "Antalgiques.db";


    static final int VERSION_BDD = 1;
    private MabaseMedicament sInstance;
    private final Context mycontext;

    private static final String TABLE_MEDIC = "table_medicaments";
    private static final String COL_ID = "ID";
    private static final String COL_NOM = "NOM";
    private static final String COL_DOSE = "DOSE";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_MEDIC + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOM + " TEXT NOT NULL, "
            + COL_DOSE + " TEXT NOT NULL);";

    // Constructeur
    MabaseMedicament(Context context) {

        super (context, NOM_BDD, null, VERSION_BDD);
        this.mycontext = context;
        File fileBase = mycontext.getDatabasePath (NOM_BDD);
        String filesDir = fileBase.getPath ();  //  context.getFilesDir ().getPath (); // par defaut: /data/data/com.package.nom/files/
        DATABASE_PATH = filesDir.substring (0, filesDir.lastIndexOf ("/")) + "/"; // /data/data/com.package.nom/databases/
        Log.d ("Constructeur: ", "filesDir=" + filesDir);
        Log.d ("Constuctucteur", "loc. de bdd: " + DATABASE_PATH);
        Log.d ("Constructeur", "Nom de la base: " + NOM_BDD);
        // Si la bdd n'existe pas dans le dossier de l'app
        if (!checkdatabase ()) {
            // copy db de 'assets' vers DATABASE_PATH
            Log.d ("Constructeur", "pas de bdd existante");
            copydatabase ();
        }
    }

    private boolean checkdatabase() {
        // retourne true/false si la bdd existe dans le dossier de l'app
        File dbfile = new File (DATABASE_PATH + NOM_BDD);
        Log.d ("checkdatabase", "verif si base existe");
        Log.d ("checkdatabase", "chemin + fichier=" + DATABASE_PATH + NOM_BDD);
        return dbfile.exists ();
    }

    // On copie la base de "assets" vers "/data/data/com.package.nom/databases"
    // ceci est fait au premier lancement de l'application
    private void copydatabase() {

        final String outFileName = DATABASE_PATH + NOM_BDD;
        Log.d ("copybase", "outFileName: " + outFileName);

        InputStream myInput ;
        OutputStream myOutput ;
        try {

            //Open your local db as the input stream
            myInput = mycontext.getAssets ().open (NOM_BDD);

            //Open the empty db as the output stream
            myOutput = new FileOutputStream (outFileName);

            // crée le repertoire
            File f = new File (DATABASE_PATH);
            f.mkdirs ();

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read (buffer)) > 0) {
                myOutput.write (buffer, 0, length);
            }
            Log.d ("copybase", "la base de donnée est recopiée, fermeture des flux");
            //Close the streams
            myOutput.flush ();
            myOutput.close ();
            myInput.close ();
        } catch (IOException e) {
            e.printStackTrace ();
            Toast.makeText (mycontext, "Erreur : copydatabase()", Toast.LENGTH_LONG).show ();
            Log.d ("copybase", "erreur sur copydatabase");
        }

        // on greffe le numéro de version
        try {
            SQLiteDatabase checkdb = SQLiteDatabase.openDatabase (outFileName, null, SQLiteDatabase.OPEN_READWRITE);
            checkdb.setVersion (VERSION_BDD);
        } catch (SQLiteException e) {
            // bdd n'existe pas
            Toast.makeText (mycontext, "Erreur : " + outFileName + " n'existe pas????", Toast.LENGTH_LONG).show ();
        }

    } // copydatabase()

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD

        db.execSQL (CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL ("DROP TABLE " + TABLE_MEDIC + ";");
        onCreate (db);
    }
}
