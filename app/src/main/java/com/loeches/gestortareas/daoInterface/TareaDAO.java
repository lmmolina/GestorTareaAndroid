package com.loeches.gestortareas.daoInterface;

import com.loeches.gestortareas.modelos.Tarea;
import com.loeches.gestortareas.modelos.Trabajador;

public interface TareaDAO {
    void AgregarTarea(Tarea ta, Trabajador tr);

    void EliminarTarea(Tarea ta);

}
