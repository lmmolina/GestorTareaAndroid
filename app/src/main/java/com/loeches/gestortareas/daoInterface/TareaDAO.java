package com.loeches.gestortareas.daoInterface;

import com.loeches.gestortareas.modelos.Tarea;
import com.loeches.gestortareas.modelos.Trabajador;

import java.util.List;

public interface TareaDAO {
    void AgregarTarea(Tarea ta, Trabajador tr);

    void EliminarTarea(Tarea ta);

    void ObtenerTarea(Trabajador tr);
}
