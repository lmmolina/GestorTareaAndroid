package com.loeches.gestortareas.conexionBD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String nombrebd = "tareas.db";
    private static int version = 1;

    public DatabaseHelper(Context context) {
        super(context, nombrebd, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Trabajador (" +
                "    DNI VARCHAR(20) PRIMARY KEY," +
                "    nombre VARCHAR(100)," +
                "    salarioHora DOUBLE," +
                "    telefono VARCHAR(20)," +
                "    correo VARCHAR(100)" +
                ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS Tarea (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fecha DATE," +
                "horaInicio TIME," +
                "horaFin TIME," +
                "lugar VARCHAR(255)," +
                "tarea TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
