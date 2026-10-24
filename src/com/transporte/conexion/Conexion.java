package com.transporte.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static String host = "localhost";
    private static String puerto = "3306";
    private static String baseDatos = "db_paqueteria";
    private static String usuario = "root";
    private static String clave = "";

    private static final String PARAMETROS = "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver de MySQL: " + e.getMessage());
        }
    }

    public static Connection getConexion() throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + puerto + "/" + baseDatos + PARAMETROS;
        return DriverManager.getConnection(url, usuario, clave);
    }

    public static void configurar(String nuevoHost, String nuevoPuerto, String nuevaBD, String nuevoUsuario, String nuevaClave) {
        host = nuevoHost;
        puerto = nuevoPuerto;
        baseDatos = nuevaBD;
        usuario = nuevoUsuario;
        clave = nuevaClave;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static String getClave() {
        return clave;
    }

    public static String getHost() {
        return host;
    }

    public static String getPuerto() {
        return puerto;
    }

    public static String getBaseDatos() {
        return baseDatos;
    }
}
