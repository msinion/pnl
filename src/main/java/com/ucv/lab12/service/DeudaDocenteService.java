package com.ucv.lab12.service;

import com.ucv.lab12.model.DeudaDocente;
import com.ucv.lab12.repository.IDeudaDocenteRepository;

import java.util.List;

public class DeudaDocenteService implements IDeudaDocenteService {
    private final IDeudaDocenteRepository repository;

    public DeudaDocenteService(IDeudaDocenteRepository repository) {
        this.repository = repository;
    }

    @Override public List<DeudaDocente> listar() { return repository.findAll(); }
    @Override public List<DeudaDocente> buscar(String tipoDeuda, String fechaRegistro, String situacionLaboral) {
        return repository.findByFilters(tipoDeuda, fechaRegistro, situacionLaboral);
    }
    @Override public void crear(DeudaDocente deuda) { validar(deuda); repository.save(deuda); }
    @Override public void actualizar(DeudaDocente deuda) { validar(deuda); repository.update(deuda); }
    @Override public void eliminar(int id) { repository.delete(id); }
    @Override public void eliminarSeleccionados(List<Integer> ids) { repository.deleteAll(ids); }

    @Override
    public void validar(DeudaDocente d) {
        if (d.getDniDocente() == null || !d.getDniDocente().matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI del docente debe tener 8 digitos.");
        }
        if (d.getNombresDocente() == null || d.getNombresDocente().trim().isEmpty()) {
            throw new IllegalArgumentException("Los nombres del docente son obligatorios.");
        }
        if (d.getTipoDeuda() == null || d.getTipoDeuda().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de deuda es obligatorio.");
        }
        if (d.getFechaRegistro() == null || !d.getFechaRegistro().matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("La fecha debe tener formato yyyy-MM-dd.");
        }
        if (d.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        if (d.getEstado() == null || d.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("El estado es obligatorio.");
        }
    }
}
