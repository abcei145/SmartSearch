package com.example.monica.smartstudents;

/**
 * Created by monica on 20/06/2016.
 */
public class Estudiantes {
    private String Nombre;
    private String Apellidos;
    private String Telefono;
    private String Correo;
    private String Contraro;
    private String ClaseActual;
    private int [] HorPendientes;
    private String ClasePendiente;
    private String[] HorProgramados;

    public Estudiantes(String nombre, String apellidos, String telefono, String correo, String contraro, String claseActual, String clasePendiente) {
        Nombre = nombre;
        Apellidos = apellidos;
        Telefono = telefono;
        HorPendientes=new int[]{0,0,0,0,0,0,0,0,0,0};
        HorProgramados=new String[10];
        Correo = correo;
        Contraro = contraro;
        ClaseActual = claseActual;
        ClasePendiente = clasePendiente;
    }

    public String[] getHorProgramados() {
        return HorProgramados;
    }

    public Estudiantes() {
    }

    public String getNombre() {
        return Nombre;
    }

    public int[] getHorPendientes() {
        return HorPendientes;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public String getTelefono() {
        return Telefono;
    }

    public String getCorreo() {
        return Correo;
    }

    public String getContraro() {
        return Contraro;
    }

    public String getClaseActual() {
        return ClaseActual;
    }

    public String getClasePendiente() {
        return ClasePendiente;
    }
}
