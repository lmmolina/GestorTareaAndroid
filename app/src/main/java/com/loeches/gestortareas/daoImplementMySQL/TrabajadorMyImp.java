package com.loeches.gestortareas.daoImplementMySQL;

import com.loeches.gestortareas.conexionBD.ConexionMySQL;
import com.loeches.gestortareas.daoInterface.TrabajadorDAO;
import com.loeches.gestortareas.modelos.Trabajador;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TrabajadorMyImp implements TrabajadorDAO {

    private ConexionMySQL conexion;

    public TrabajadorMyImp(ConexionMySQL conexion) {
        this.conexion = conexion;
    }

    @Override
    public void InsertarTrabajador(Trabajador t) {
        try {
            PreparedStatement statement = conexion.getConexion().prepareStatement(
                    "INSERT INTO trabajador (DNI, nombre, salarioHora, telefono, correo)  " +
                            "VALUES (?, ?, ?, ?, ?);"
            );

            statement.setString(1, t.getDNI());
            statement.setString(2, t.getNombre());
            statement.setDouble(3, t.getSalarioHora());
            statement.setString(4, t.getTelefono());
            statement.setString(5, t.getCorreo());
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void EliminarTrabajador(String DNI) {
        try {
            PreparedStatement statement = conexion.getConexion().prepareStatement(
                    "DELETE FROM trabajador WHERE DNI = ?;"
            );
            statement.setString(1, DNI);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ActualizarTrabajador(Trabajador t) {
        try {
            PreparedStatement statement = conexion.getConexion().prepareStatement(
                    "UPDATE trabajador SET " +
                            "nombre = ?," +
                            "salarioHora = ?," +
                            "telefono = ?," +
                            "correo = ? " +
                            "WHERE DNI = ?;"
            );
            statement.setString(5, t.getDNI());
            statement.setString(1, t.getNombre());
            statement.setDouble(2, t.getSalarioHora());
            statement.setString(3, t.getTelefono());
            statement.setString(4, t.getCorreo());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Trabajador ObtenerTrabajador(String DNI) {
        Trabajador tra = null;
        try {
            PreparedStatement statement = conexion.getConexion().prepareStatement(
                    "SELECT * FROM trabajador WHERE DNI = ?;"
            );
            statement.setString(1, DNI);
            ResultSet cursor = statement.executeQuery();
            if (cursor.next()) {
                String nombre = cursor.getString("nombre");
                double salarioHora = cursor.getDouble("salarioHora");
                String telefono = cursor.getString("telefono");
                String correo = cursor.getString("correo");
                tra = new Trabajador(nombre, salarioHora, DNI, telefono, correo);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tra;
    }

    @Override
    public List<Trabajador> ObtenerTrabajadores() {
        List<Trabajador> trabajadores = new ArrayList<>();
        try {
            PreparedStatement statement = conexion.getConexion().prepareStatement(
                    "SELECT * FROM trabajador;"
            );
            ResultSet cursor = statement.executeQuery();
            while (cursor.next()) {
                String DNI = cursor.getString("DNI");
                String nombre = cursor.getString("nombre");
                double salarioHora = cursor.getDouble("salarioHora");
                String telefono = cursor.getString("telefono");
                String correo = cursor.getString("correo");
                trabajadores.add(new Trabajador(nombre, salarioHora, DNI, telefono, correo));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trabajadores;
    }
}
