package com.transporte.dao;

import com.transporte.conexion.Conexion;
import com.transporte.modelo.Camion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CamionDAO {

    public boolean insertar(Camion camion) throws SQLException {
        String sql = "INSERT INTO camion (matricula, modelo, tipo, potencia) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, camion.getMatricula().trim());
            ps.setString(2, camion.getModelo().trim());
            ps.setString(3, camion.getTipo().trim());
            ps.setInt(4, camion.getPotencia());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Camion> listar() throws SQLException {
        List<Camion> lista = new ArrayList<>();
        String sql = "SELECT matricula, modelo, tipo, potencia FROM camion ORDER BY matricula ASC";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Camion c = new Camion(
                        rs.getString("matricula"),
                        rs.getString("modelo"),
                        rs.getString("tipo"),
                        rs.getInt("potencia")
                );
                lista.add(c);
            }
        }
        return lista;
    }

    public Camion obtenerPorMatricula(String matricula) throws SQLException {
        String sql = "SELECT matricula, modelo, tipo, potencia FROM camion WHERE matricula = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Camion(
                            rs.getString("matricula"),
                            rs.getString("modelo"),
                            rs.getString("tipo"),
                            rs.getInt("potencia")
                    );
                }
            }
        }
        return null;
    }

    public boolean actualizar(Camion camion) throws SQLException {
        String sql = "UPDATE camion SET modelo = ?, tipo = ?, potencia = ? WHERE matricula = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, camion.getModelo().trim());
            ps.setString(2, camion.getTipo().trim());
            ps.setInt(3, camion.getPotencia());
            ps.setString(4, camion.getMatricula().trim());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String matricula) throws SQLException {
        String sql = "DELETE FROM camion WHERE matricula = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula.trim());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existe(String matricula) throws SQLException {
        return obtenerPorMatricula(matricula) != null;
    }
}
