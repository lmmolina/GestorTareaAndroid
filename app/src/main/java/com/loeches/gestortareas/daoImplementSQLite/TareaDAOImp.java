package com.loeches.gestortareas.daoImplementSQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.loeches.gestortareas.daoInterface.TareaDAO;
import com.loeches.gestortareas.modelos.Tarea;
import com.loeches.gestortareas.modelos.Trabajador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TareaDAOImp implements TareaDAO {
    private SQLiteDatabase db;

    public TareaDAOImp(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void AgregarTarea(Tarea ta, Trabajador tr) {
        ContentValues values = new ContentValues();
        values.put("fecha", ta.getFecha().format(DateTimeFormatter.ofPattern("yyy-MM-dd")));
        values.put("horaInicio", ta.getHoraInicio().format(DateTimeFormatter.ISO_LOCAL_TIME));
        values.put("horaFin", ta.getHoraFin().format(DateTimeFormatter.ISO_LOCAL_TIME));
        values.put("lugar", ta.getLugar());
        values.put("tarea", ta.getTarea());
        values.put("DNI", tr.getDNI());
        int id = (int) db.insert("Tarea", null, values);
        ta.setId(id);
        tr.getTareas().add(ta);
    }

    @Override
    public void EliminarTarea(Tarea ta) {
        db.delete("Tarea", "id = ?", new String[]{ta.getId() + ""});
    }

    @Override
    public void ObtenerTareas(Trabajador tr) {
        Cursor cursor = db.query("Tarea", null, "DNI = ?", new String[]{tr.getDNI()},
                null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            LocalDate fecha =
                    LocalDate.parse(cursor.getString(1), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime horaInicio =
                    LocalTime.parse(cursor.getString(2), DateTimeFormatter.ISO_LOCAL_TIME);
            LocalTime horaFin =
                    LocalTime.parse(cursor.getString(3), DateTimeFormatter.ISO_LOCAL_TIME);
            String lugar = cursor.getString(4);
            String tarea = cursor.getString(5);
            tr.getTareas().add(new Tarea(id, fecha, horaInicio, horaFin, lugar, tarea));
        }
    }
}
