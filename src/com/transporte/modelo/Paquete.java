package com.transporte.modelo;

public class Paquete {
    private int codigo;
    private String descripcion;
    private String destinatario;
    private String direccionDestinatario;
    private String dniCamionero;
    private String nombreCamionero;

    public Paquete() {
    }

    public Paquete(int codigo, String descripcion, String destinatario, String direccionDestinatario, String dniCamionero) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.destinatario = destinatario;
        this.direccionDestinatario = direccionDestinatario;
        this.dniCamionero = dniCamionero;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getDireccionDestinatario() {
        return direccionDestinatario;
    }

    public void setDireccionDestinatario(String direccionDestinatario) {
        this.direccionDestinatario = direccionDestinatario;
    }

    public String getDniCamionero() {
        return dniCamionero;
    }

    public void setDniCamionero(String dniCamionero) {
        this.dniCamionero = dniCamionero;
    }

    public String getNombreCamionero() {
        return nombreCamionero;
    }

    public void setNombreCamionero(String nombreCamionero) {
        this.nombreCamionero = nombreCamionero;
    }

    @Override
    public String toString() {
        return "Paquete #" + codigo + " (" + destinatario + ")";
    }
}
