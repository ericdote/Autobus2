package com.example.eric.autobus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Eric on 07/02/2017.
 */

public class SqlClass extends SQLiteOpenHelper {

    String tablaUsersInterna = "CREATE TABLE tablaUsersInterna(matricula TEXT PRIMARY KEY, password TEXT)";
    String tablaPosicioInterna = "CREATE TABLE tablaPosicio(matricula TEXT, latitud TEXT, longitud TEXT, fecha DATE)";

    public SqlClass(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(tablaUsersInterna);

        db.execSQL("INSERT INTO tablaUsersInterna VALUES ('1111AAA', 'hola')");
        db.execSQL("INSERT INTO tablaUsersInterna VALUES ('2222BBB', 'adios')");
        db.execSQL("INSERT INTO tablaUsersInterna VALUES ('3333CCC', 'hola')");

        db.execSQL(tablaPosicioInterna);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}