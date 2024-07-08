package com.loeches.gestortareas.conexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {
    private Connection conexion;

    public ConexionMySQL() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conexion = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/tareas", "remoto", "");
    }

    public Connection getConexion() {
        return conexion;
    }

    public void CerrarConexion() {
        try {
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
