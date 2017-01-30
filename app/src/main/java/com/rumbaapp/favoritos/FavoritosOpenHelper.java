package com.rumbaapp.favoritos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rangel on 02/08/2016.
 */
public class FavoritosOpenHelper extends SQLiteOpenHelper {
    String tabla_favoritos="create table favoritos(codigo_sitio integer primary key,imagen_sitio text,nombre_sitio text,direccion_sitio text,calificacion_sitio real)";
    public FavoritosOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(tabla_favoritos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists favoritos");
        onCreate(db);
    }
}
