package com.transporte.modelo;

import java.time.LocalDate;

public class CamioneroCamion {
    private int id;
    private String dniCamionero;
    private String matriculaCamion;
    private LocalDate fecha;
    private String nombreCamionero;
    private String modeloCamion;

    public CamioneroCamion() {
    }

    public CamioneroCamion(int id, String dniCamionero, String matriculaCamion, LocalDate fecha) {
        this.id = id;
        this.dniCamionero = dniCamionero;
        this.matriculaCamion = matriculaCamion;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDniCamionero() {
        return dniCamionero;
    }

    public void setDniCamionero(String dniCamionero) {
        this.dniCamionero = dniCamionero;
    }

    public String getMatriculaCamion() {
        return matriculaCamion;
    }

    public void setMatriculaCamion(String matriculaCamion) {
        this.matriculaCamion = matriculaCamion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getNombreCamionero() {
        return nombreCamionero;
    }

    public void setNombreCamionero(String nombreCamionero) {
        this.nombreCamionero = nombreCamionero;
    }

    public String getModeloCamion() {
        return modeloCamion;
    }

    public void setModeloCamion(String modeloCamion) {
        this.modeloCamion = modeloCamion;
    }

    @Override
    public String toString() {
        return "Asignacion " + id + ": " + dniCamionero + " - " + matriculaCamion + " (" + fecha + ")";
    }
}
