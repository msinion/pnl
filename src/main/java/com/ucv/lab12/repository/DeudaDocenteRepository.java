package com.ucv.lab12.repository;

import com.ucv.lab12.config.DatabaseConfig;
import com.ucv.lab12.model.DeudaDocente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeudaDocenteRepository implements IDeudaDocenteRepository, AutoCloseable {
    private final DatabaseConfig dbConfig;

    public DeudaDocenteRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public List<DeudaDocente> findAll() {
        return findByFilters("", "", "");
    }

    @Override
    public List<DeudaDocente> findByFilters(String tipoDeuda, String fechaRegistro, String situacionLaboral) {
        String sql = """
                SELECT IdDeuda, DniDocente, NombresDocente, TipoDeuda, FechaRegistro,
                       Monto, Estado, SituacionLaboral, Observacion
                FROM DeudaDocente
                WHERE TipoDeuda LIKE ?
                  AND CONVERT(VARCHAR(10), FechaRegistro, 23) LIKE ?
                  AND SituacionLaboral LIKE ?
                ORDER BY FechaRegistro DESC, NombresDocente
                """;
        List<DeudaDocente> deudas = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nvl(tipoDeuda) + "%");
            ps.setString(2, "%" + nvl(fechaRegistro) + "%");
            ps.setString(3, "%" + nvl(situacionLaboral) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) deudas.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar deudas docentes.", e);
        }
        return deudas;
    }

    @Override
    public void save(DeudaDocente d) {
        String sql = """
                INSERT INTO DeudaDocente
                (DniDocente, NombresDocente, TipoDeuda, FechaRegistro, Monto, Estado, SituacionLaboral, Observacion)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        executeSaveOrUpdate(sql, d, false);
    }

    @Override
    public void update(DeudaDocente d) {
        String sql = """
                UPDATE DeudaDocente
                SET DniDocente = ?, NombresDocente = ?, TipoDeuda = ?, FechaRegistro = ?,
                    Monto = ?, Estado = ?, SituacionLaboral = ?, Observacion = ?
                WHERE IdDeuda = ?
                """;
        executeSaveOrUpdate(sql, d, true);
    }

    private void executeSaveOrUpdate(String sql, DeudaDocente d, boolean update) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nvl(d.getDniDocente()));
            ps.setString(2, nvl(d.getNombresDocente()));
            ps.setString(3, nvl(d.getTipoDeuda()));
            ps.setString(4, nvl(d.getFechaRegistro()));
            ps.setDouble(5, d.getMonto());
            ps.setString(6, nvl(d.getEstado()));
            ps.setString(7, nvl(d.getSituacionLaboral()));
            ps.setString(8, nvl(d.getObservacion()));
            if (update) ps.setInt(9, d.getIdDeuda());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar deuda docente: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int idDeuda) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM DeudaDocente WHERE IdDeuda = ?")) {
            ps.setInt(1, idDeuda);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar deuda docente.", e);
        }
    }

    @Override
    public void deleteAll(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sql = new StringBuilder("DELETE FROM DeudaDocente WHERE IdDeuda IN (");
        for (int i = 0; i < ids.size(); i++) sql.append(i == 0 ? "?" : ",?");
        sql.append(")");
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) ps.setInt(i + 1, ids.get(i));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar deudas seleccionadas.", e);
        }
    }

    private DeudaDocente mapRow(ResultSet rs) throws SQLException {
        return new DeudaDocente(
                rs.getInt("IdDeuda"),
                rs.getString("DniDocente"),
                rs.getString("NombresDocente"),
                rs.getString("TipoDeuda"),
                rs.getString("FechaRegistro"),
                rs.getDouble("Monto"),
                rs.getString("Estado"),
                rs.getString("SituacionLaboral"),
                rs.getString("Observacion")
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
