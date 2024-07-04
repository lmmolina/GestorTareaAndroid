package com.loeches.gestortareas.daoImplementMySQL;

import com.loeches.gestortareas.conexionBD.ConexionMySQL;
import com.loeches.gestortareas.daoInterface.TareaDAO;
import com.loeches.gestortareas.modelos.Tarea;
import com.loeches.gestortareas.modelos.Trabajador;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TareaMyImp implements TareaDAO {
    private ConexionMySQL conexion;

    public TareaMyImp(ConexionMySQL conexion) {
        this.conexion = conexion;
    }

    @Override
    public void AgregarTarea(Tarea ta, Trabajador tr) {
        try {
            PreparedStatement statement = conexion.getConexion().prepareStatement(
                    "INSERT INTO tarea (fecha, horaInicio, horaFin, lugar, tarea, DNI) " +
                            "VALUES (?, ?, ?, ?, ?, ?);"
            );
            statement.setDate(1, Date.valueOf(ta.getFecha().toString()));
            //statement.setString(1, ta.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            statement.setTime(2, Time.valueOf(ta.getHoraInicio().toString()));
            statement.setTime(3, Time.valueOf(ta.getHoraFin().toString()));
            statement.setString(4, ta.getLugar());
            statement.setString(5, ta.getTarea());
            statement.setString(6, tr.getDNI());
            statement.executeUpdate();

            ResultSet ids = statement.getGeneratedKeys();
            ids.next();
            ta.setId(ids.getInt(1));

            tr.getTareas().add(ta);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void EliminarTarea(Tarea ta) {
        try {
            PreparedStatement statement = conexion.getConexion().prepareStatement(
                    "DELETE FROM tarea WHERE id = ?;"
            );
            statement.setInt(1, ta.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ObtenerTareas(Trabajador tr) {
        try {
            PreparedStatement statement = conexion.getConexion().prepareStatement(
                    "SELECT * FROM tarea WHERE DNI = ?;"
            );
            statement.setString(1, tr.getDNI());
            ResultSet cursor = statement.executeQuery();
            while (cursor.next()) {
                int id = cursor.getInt("id");
                LocalDate fecha = LocalDate.parse(cursor.getString("fecha"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalTime horaInicio = LocalTime.parse(cursor.getString("horaInicio"),
                        DateTimeFormatter.ISO_LOCAL_TIME);
                LocalTime horaFin = LocalTime.parse(cursor.getString("horaFin"),
                        DateTimeFormatter.ISO_LOCAL_TIME);
                String lugar = cursor.getString("lugar");
                String tarea = cursor.getString("tarea");
                tr.getTareas().add(new Tarea(id, fecha, horaInicio, horaFin, lugar, tarea));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
