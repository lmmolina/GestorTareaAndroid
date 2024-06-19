package com.loeches.gestortareas.daoImplement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.loeches.gestortareas.daoInterface.TrabajadorDAO;
import com.loeches.gestortareas.modelos.Trabajador;

import java.util.ArrayList;
import java.util.List;

public class TrabajadorDAOImp implements TrabajadorDAO {
    private SQLiteDatabase db;

    public TrabajadorDAOImp(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void InsertarTrabajador(Trabajador t) {
        ContentValues values = new ContentValues();
        values.put("DNI", t.getDNI());
        values.put("nombre", t.getNombre());
        values.put("salarioHora", t.getSalarioHora());
        values.put("telefono", t.getTelefono());
        values.put("correo", t.getCorreo());
        db.insert("Trabajador", null, values);
    }

    @Override
    public void EliminarTrabajador(String DNI) {
        db.delete("Trabajador", "DNI = ?", new String[]{DNI});
    }

    @Override
    public void ActualizarTrabajador(Trabajador t) {
        ContentValues values = new ContentValues();
        values.put("nombre", t.getNombre());
        values.put("salarioHora", t.getSalarioHora());
        values.put("telefono", t.getTelefono());
        values.put("correo", t.getCorreo());
        db.update("Trabajador", values, "DNI = ?", new String[]{t.getDNI()});
    }

    public void AumentarSalario(int porcentaje, int max) {
        double por = 1 + porcentaje / 100.0;
        db.execSQL("UPDATE Trabajador SET salarioHora = salarioHora * ? WHERE salario < ?",
                new String[]{por + "", max + ""});
    }

    public double PromedioSalario() {
        Cursor cursor = db.rawQuery("SELECT AVG(salarioHora), SUM(salarioHora) FROM Trabajador", null);
        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        return -1;
    }

    @Override
    public Trabajador ObtenerTrabajador(String DNI) {
        // Esta linea es exactamente lo mismo que la siguiente
        // Cursor cursor = db.rawQuery("SELECT * FROM Trabajador WHERE DNI = ?", new String[]{DNI});
        Cursor cursor = db.query("Trabajador", null, "DNI=?", new String[]{DNI}, null, null, null);
        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            double salarioHora = cursor.getDouble(cursor.getColumnIndexOrThrow("salarioHora"));
            String telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"));
            String correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
            return new Trabajador(nombre, salarioHora, DNI, telefono, correo);
        }
        return null;
    }

    @Override
    public List<Trabajador> ObtenerTrabajadores() {
        List<Trabajador> trabajadores = new ArrayList<>();
        Cursor cursor = db.query("Trabajador", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String DNI = cursor.getString(cursor.getColumnIndexOrThrow("DNI"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            double salarioHora = cursor.getDouble(cursor.getColumnIndexOrThrow("salarioHora"));
            String telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"));
            String correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
            trabajadores.add(new Trabajador(nombre, salarioHora, DNI, telefono, correo));
        }
        cursor.close();
        return trabajadores;
    }
}
