package com.ucv.lab12.service;

import com.ucv.lab12.model.Docente;
import java.util.List;

public interface IDocenteService {
    List<Docente> listar();
    List<Docente> buscar(String nombres, String dni);
    void crear(Docente docente);
    void actualizar(Docente docente);
    void eliminar(int id);
    void eliminarSeleccionados(List<Integer> ids);
    void validar(Docente docente);
}
