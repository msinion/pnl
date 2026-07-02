package com.ucv.lab12.repository;

import com.ucv.lab12.model.Docente;
import java.util.List;

public interface IDocenteRepository {
    List<Docente> findAll();
    List<Docente> findByFilters(String nombres, String dni);
    void save(Docente docente);
    void update(Docente docente);
    void delete(int idDocente);
    void deleteAll(List<Integer> ids);
}
