package com.loeches.gestortareas.modelos;

import java.util.ArrayList;
import java.util.List;

public class Trabajador {
    private String nombre;
    private double salarioHora;
    private String DNI;
    private String telefono;
    private String correo;
    List<Tarea> tareas;

    public Trabajador(String nombre, double salarioHora, String DNI, String telefono, String correo) {
        this.nombre = nombre;
        this.salarioHora = salarioHora;
        this.DNI = DNI;
        this.telefono = telefono;
        this.correo = correo;
        tareas=new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getSalarioHora() {
        return salarioHora;
    }

    public void setSalarioHora(double salarioHora) {
        this.salarioHora = salarioHora;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }
}
