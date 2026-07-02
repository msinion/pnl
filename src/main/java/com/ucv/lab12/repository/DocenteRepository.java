package com.ucv.lab12.repository;

import com.ucv.lab12.config.DatabaseConfig;
import com.ucv.lab12.model.Docente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocenteRepository implements IDocenteRepository, AutoCloseable {
    private final DatabaseConfig dbConfig;

    public DocenteRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public List<Docente> findAll() {
        return findByFilters("", "");
    }

    @Override
    public List<Docente> findByFilters(String nombres, String dni) {
        String sql = """
                SELECT IdDocente, Nombres, DNI, Celular, Email, SituacionLaboral, Activo
                FROM Docente
                WHERE Nombres LIKE ? AND DNI LIKE ?
                ORDER BY Nombres
                """;
        List<Docente> docentes = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nvl(nombres) + "%");
            ps.setString(2, "%" + nvl(dni) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) docentes.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar docentes.", e);
        }
        return docentes;
    }

    @Override
    public void save(Docente d) {
        String sql = """
                INSERT INTO Docente (Nombres, DNI, Celular, Email, SituacionLaboral, Activo)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nvl(d.getNombres()));
            ps.setString(2, nvl(d.getDni()));
            ps.setString(3, nvl(d.getCelular()));
            ps.setString(4, nvl(d.getEmail()));
            ps.setString(5, nvl(d.getSituacionLaboral()));
            ps.setBoolean(6, d.isActivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar docente: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Docente d) {
        String sql = """
                UPDATE Docente
                SET Nombres = ?, DNI = ?, Celular = ?, Email = ?,
                    SituacionLaboral = ?, Activo = ?
                WHERE IdDocente = ?
                """;
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nvl(d.getNombres()));
            ps.setString(2, nvl(d.getDni()));
            ps.setString(3, nvl(d.getCelular()));
            ps.setString(4, nvl(d.getEmail()));
            ps.setString(5, nvl(d.getSituacionLaboral()));
            ps.setBoolean(6, d.isActivo());
            ps.setInt(7, d.getIdDocente());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar docente: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int idDocente) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Docente WHERE IdDocente = ?")) {
            ps.setInt(1, idDocente);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar docente.", e);
        }
    }

    @Override
    public void deleteAll(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sql = new StringBuilder("DELETE FROM Docente WHERE IdDocente IN (");
        for (int i = 0; i < ids.size(); i++) sql.append(i == 0 ? "?" : ",?");
        sql.append(")");
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) ps.setInt(i + 1, ids.get(i));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar docentes seleccionados.", e);
        }
    }

    private Docente mapRow(ResultSet rs) throws SQLException {
        return new Docente(
                rs.getInt("IdDocente"),
                rs.getString("Nombres"),
                rs.getString("DNI"),
                rs.getString("Celular"),
                rs.getString("Email"),
                rs.getString("SituacionLaboral"),
                rs.getBoolean("Activo")
        );
    }

    private String nvl(String value) {
        return value == null ? "" : value.trim();
    }

    @Override
    public void close() {
        dbConfig.close();
    }
}
