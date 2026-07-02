package com.ucv.lab12.repository;

import com.ucv.lab12.model.DeudaDocente;
import java.util.List;

public interface IDeudaDocenteRepository {
    List<DeudaDocente> findAll();
    List<DeudaDocente> findByFilters(String tipoDeuda, String fechaRegistro, String situacionLaboral);
    void save(DeudaDocente deuda);
    void update(DeudaDocente deuda);
    void delete(int idDeuda);
    void deleteAll(List<Integer> ids);
}
