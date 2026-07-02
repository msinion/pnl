package com.ucv.lab12.service;

import com.ucv.lab12.model.Docente;
import com.ucv.lab12.repository.IDocenteRepository;

import java.util.List;

public class DocenteService implements IDocenteService {
    private final IDocenteRepository repository;

    public DocenteService(IDocenteRepository repository) {
        this.repository = repository;
    }

    @Override public List<Docente> listar() { return repository.findAll(); }
    @Override public List<Docente> buscar(String nombres, String dni) { return repository.findByFilters(nombres, dni); }
    @Override public void crear(Docente docente) { validar(docente); repository.save(docente); }
    @Override public void actualizar(Docente docente) { validar(docente); repository.update(docente); }
    @Override public void eliminar(int id) { repository.delete(id); }
    @Override public void eliminarSeleccionados(List<Integer> ids) { repository.deleteAll(ids); }

    @Override
    public void validar(Docente d) {
        if (d.getNombres() == null || d.getNombres().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del docente es obligatorio.");
        }
        if (d.getDni() == null || !d.getDni().matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener 8 digitos.");
        }
        if (d.getEmail() != null && !d.getEmail().trim().isEmpty()
                && !d.getEmail().matches("^[\\w.+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("El email no tiene formato valido.");
        }
        if (d.getSituacionLaboral() == null || d.getSituacionLaboral().trim().isEmpty()) {
            throw new IllegalArgumentException("La situacion laboral es obligatoria.");
        }
    }
}
