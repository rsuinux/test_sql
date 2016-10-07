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
 * Revised 07 oct 2016
 */

class MabaseMedicament extends SQLiteOpenHelper {

    private String DATABASE_PATH; // chemin défini dans le constructeur
    private static final String NOM_BDD = "Antalgiques.db";


    static final int VERSION_BDD = 1;
    public static MabaseMedicament sInstance;
    private final Context mycontext;

    private static final String TABLE_MEDIC = "table_medicaments";
    private static final String COL_ID = "ID";
    private static final String COL_NOM = "NOM";
    private static final String COL_DOSE = "DOSE";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_MEDIC + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOM + " TEXT NOT NULL, "
            + COL_DOSE + " TEXT NOT NULL);";

    public static synchronized MabaseMedicament getInstance(Context context) {
        Log.d ("getInstance", "création de la base");

        if (sInstance == null) {
            sInstance = new MabaseMedicament (context);
        }
        return sInstance;
    }

    // Constructeur
    MabaseMedicament(Context context) {
        super (context, NOM_BDD, null, VERSION_BDD);
        this.mycontext = context;
        String filesDir = context.getFilesDir ().getPath (); // /data/data/com.package.nom/files/
        DATABASE_PATH = filesDir.substring (0, filesDir.lastIndexOf ("/")) + "/databases/"; // /data/data/com.package.nom/databases/
        Log.d ("ConstR MabaseMedicament", "création de la base");

        // Si la bdd n'existe pas dans le dossier de l'app
        if (!checkdatabase ()) {
            // copy db de 'assets' vers DATABASE_PATH
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
    private boolean copydatabase() {

        final String outFileName = DATABASE_PATH + NOM_BDD;
        Log.d ("copybase", "outFileName: " + outFileName);

        InputStream myInput;
        try {
            // Ouvre la bdd de 'assets' en lecture
            myInput = mycontext.getAssets ().open (NOM_BDD);

            // dossier de destination
            File pathFile = new File (DATABASE_PATH);
            if (!pathFile.exists ()) {
                if (!pathFile.mkdirs ()) {
                    Toast.makeText (mycontext, "Erreur : copydatabase(), pathFile.mkdirs()", Toast.LENGTH_SHORT).show ();
                    return false;
                }
            }

            // Ouverture en écriture du fichier bdd de destination
            OutputStream myOutput = new FileOutputStream (outFileName);

            // transfert de inputfile vers outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read (buffer)) > 0) {
                myOutput.write (buffer, 0, length);
            }

            // Fermeture
            myOutput.flush ();
            myOutput.close ();
            myInput.close ();
        } catch (IOException e) {
            e.printStackTrace ();
            Toast.makeText (mycontext, "Erreur : copydatabase()", Toast.LENGTH_SHORT).show ();
            return false;
        }

        // on greffe le numéro de version
        try {
            SQLiteDatabase checkdb = SQLiteDatabase.openDatabase (DATABASE_PATH + NOM_BDD, null, SQLiteDatabase.OPEN_READWRITE);
            checkdb.setVersion (VERSION_BDD);
        } catch (SQLiteException e) {
            // bdd n'existe pas
            return false;
        }

        return true;
    } // copydatabase()

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
        Log.d ("onCreate", "pas de bdd existante");
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
