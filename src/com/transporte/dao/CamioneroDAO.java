package com.transporte.dao;

import com.transporte.conexion.Conexion;
import com.transporte.modelo.Camionero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CamioneroDAO {

    public boolean insertar(Camionero camionero) throws SQLException {
        String sql = "INSERT INTO camionero (dni, nombre, telefono, direccion, salario, poblacion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, camionero.getDni().trim());
            ps.setString(2, camionero.getNombre().trim());
            ps.setString(3, camionero.getTelefono().trim());
            ps.setString(4, camionero.getDireccion().trim());
            ps.setDouble(5, camionero.getSalario());
            ps.setString(6, camionero.getPoblacion().trim());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Camionero> listar() throws SQLException {
        List<Camionero> lista = new ArrayList<>();
        String sql = "SELECT dni, nombre, telefono, direccion, salario, poblacion FROM camionero ORDER BY nombre ASC";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Camionero c = new Camionero(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getDouble("salario"),
                        rs.getString("poblacion")
                );
                lista.add(c);
            }
        }
        return lista;
    }

    public Camionero obtenerPorDni(String dni) throws SQLException {
        String sql = "SELECT dni, nombre, telefono, direccion, salario, poblacion FROM camionero WHERE dni = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Camionero(
                            rs.getString("dni"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("direccion"),
                            rs.getDouble("salario"),
                            rs.getString("poblacion")
                    );
                }
            }
        }
        return null;
    }

    public boolean actualizar(Camionero camionero) throws SQLException {
        String sql = "UPDATE camionero SET nombre = ?, telefono = ?, direccion = ?, salario = ?, poblacion = ? WHERE dni = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, camionero.getNombre().trim());
            ps.setString(2, camionero.getTelefono().trim());
            ps.setString(3, camionero.getDireccion().trim());
            ps.setDouble(4, camionero.getSalario());
            ps.setString(5, camionero.getPoblacion().trim());
            ps.setString(6, camionero.getDni().trim());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String dni) throws SQLException {
        String sql = "DELETE FROM camionero WHERE dni = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni.trim());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existe(String dni) throws SQLException {
        return obtenerPorDni(dni) != null;
    }
}
