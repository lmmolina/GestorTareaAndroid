package com.loeches.gestortareas.daoInterface;

import com.loeches.gestortareas.modelos.Trabajador;

import java.util.List;

public interface TrabajadorDAO {
    void InsertarTrabajador(Trabajador t);

    void EliminarTrabajador(String DNI);

    void ActualizarTrabajador(Trabajador t);

    Trabajador ObtenerTrabajador(String DNI);

    List<Trabajador> ObtenerTrabajadores();
}
