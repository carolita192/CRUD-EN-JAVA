package com.transporte.dao;

import com.transporte.conexion.Conexion;
import com.transporte.modelo.CamioneroCamion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CamioneroCamionDAO {

    public boolean insertar(CamioneroCamion cc) throws SQLException {
        String sql = "INSERT INTO camionero_camion (dni_camionero, matricula_camion, fecha) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cc.getDniCamionero().trim());
            ps.setString(2, cc.getMatriculaCamion().trim());
            ps.setDate(3, Date.valueOf(cc.getFecha()));
            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        cc.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public List<CamioneroCamion> listar() throws SQLException {
        List<CamioneroCamion> lista = new ArrayList<>();
        String sql = "SELECT cc.id, cc.dni_camionero, cc.matricula_camion, cc.fecha, " +
                     "c.nombre AS nombre_camionero, " +
                     "cam.modelo AS modelo_camion " +
                     "FROM camionero_camion cc " +
                     "INNER JOIN camionero c ON cc.dni_camionero = c.dni " +
                     "INNER JOIN camion cam ON cc.matricula_camion = cam.matricula " +
                     "ORDER BY cc.fecha DESC, cc.id DESC";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CamioneroCamion cc = new CamioneroCamion(
                        rs.getInt("id"),
                        rs.getString("dni_camionero"),
                        rs.getString("matricula_camion"),
                        rs.getDate("fecha").toLocalDate()
                );
                cc.setNombreCamionero(rs.getString("nombre_camionero"));
                cc.setModeloCamion(rs.getString("modelo_camion"));
                lista.add(cc);
            }
        }
        return lista;
    }

    public CamioneroCamion obtenerPorId(int id) throws SQLException {
        String sql = "SELECT cc.id, cc.dni_camionero, cc.matricula_camion, cc.fecha, " +
                     "c.nombre AS nombre_camionero, " +
                     "cam.modelo AS modelo_camion " +
                     "FROM camionero_camion cc " +
                     "INNER JOIN camionero c ON cc.dni_camionero = c.dni " +
                     "INNER JOIN camion cam ON cc.matricula_camion = cam.matricula " +
                     "WHERE cc.id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CamioneroCamion cc = new CamioneroCamion(
                            rs.getInt("id"),
                            rs.getString("dni_camionero"),
                            rs.getString("matricula_camion"),
                            rs.getDate("fecha").toLocalDate()
                    );
                    cc.setNombreCamionero(rs.getString("nombre_camionero"));
                    cc.setModeloCamion(rs.getString("modelo_camion"));
                    return cc;
                }
            }
        }
        return null;
    }

    public boolean actualizar(CamioneroCamion cc) throws SQLException {
        String sql = "UPDATE camionero_camion SET dni_camionero = ?, matricula_camion = ?, fecha = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cc.getDniCamionero().trim());
            ps.setString(2, cc.getMatriculaCamion().trim());
            ps.setDate(3, Date.valueOf(cc.getFecha()));
            ps.setInt(4, cc.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM camionero_camion WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existeAsignacion(String dni, String matricula, LocalDate fecha, int idIgnorar) throws SQLException {
        String sql = "SELECT COUNT(*) FROM camionero_camion WHERE dni_camionero = ? AND matricula_camion = ? AND fecha = ? AND id <> ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni.trim());
            ps.setString(2, matricula.trim());
            ps.setDate(3, Date.valueOf(fecha));
            ps.setInt(4, idIgnorar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
