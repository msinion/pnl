package com.ucv.lab12.service;

import com.ucv.lab12.model.DeudaDocente;
import java.util.List;

public interface IDeudaDocenteService {
    List<DeudaDocente> listar();
    List<DeudaDocente> buscar(String tipoDeuda, String fechaRegistro, String situacionLaboral);
    void crear(DeudaDocente deuda);
    void actualizar(DeudaDocente deuda);
    void eliminar(int id);
    void eliminarSeleccionados(List<Integer> ids);
    void validar(DeudaDocente deuda);
}
