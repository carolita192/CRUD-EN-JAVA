package com.transporte.dao;

import com.transporte.conexion.Conexion;
import com.transporte.modelo.Paquete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PaqueteDAO {

    public boolean insertar(Paquete paquete) throws SQLException {
        String sql = "INSERT INTO paquete (descripcion, destinatario, direccion_destinatario, dni_camionero) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, paquete.getDescripcion().trim());
            ps.setString(2, paquete.getDestinatario().trim());
            ps.setString(3, paquete.getDireccionDestinatario().trim());
            ps.setString(4, paquete.getDniCamionero().trim());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        paquete.setCodigo(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public List<Paquete> listar() throws SQLException {
        List<Paquete> lista = new ArrayList<>();
        String sql = "SELECT p.codigo, p.descripcion, p.destinatario, p.direccion_destinatario, p.dni_camionero, " +
                     "c.nombre AS nombre_camionero " +
                     "FROM paquete p " +
                     "INNER JOIN camionero c ON p.dni_camionero = c.dni " +
                     "ORDER BY p.codigo DESC";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Paquete p = new Paquete(
                        rs.getInt("codigo"),
                        rs.getString("descripcion"),
                        rs.getString("destinatario"),
                        rs.getString("direccion_destinatario"),
                        rs.getString("dni_camionero")
                );
                p.setNombreCamionero(rs.getString("nombre_camionero"));
                lista.add(p);
            }
        }
        return lista;
    }

    public Paquete obtenerPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT p.codigo, p.descripcion, p.destinatario, p.direccion_destinatario, p.dni_camionero, " +
                     "c.nombre AS nombre_camionero " +
                     "FROM paquete p " +
                     "INNER JOIN camionero c ON p.dni_camionero = c.dni " +
                     "WHERE p.codigo = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Paquete p = new Paquete(
                            rs.getInt("codigo"),
                            rs.getString("descripcion"),
                            rs.getString("destinatario"),
                            rs.getString("direccion_destinatario"),
                            rs.getString("dni_camionero")
                    );
                    p.setNombreCamionero(rs.getString("nombre_camionero"));
                    return p;
                }
            }
        }
        return null;
    }

    public boolean actualizar(Paquete paquete) throws SQLException {
        String sql = "UPDATE paquete SET descripcion = ?, destinatario = ?, direccion_destinatario = ?, dni_camionero = ? WHERE codigo = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, paquete.getDescripcion().trim());
            ps.setString(2, paquete.getDestinatario().trim());
            ps.setString(3, paquete.getDireccionDestinatario().trim());
            ps.setString(4, paquete.getDniCamionero().trim());
            ps.setInt(5, paquete.getCodigo());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int codigo) throws SQLException {
        String sql = "DELETE FROM paquete WHERE codigo = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }
}
