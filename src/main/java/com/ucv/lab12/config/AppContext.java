package com.ucv.lab12.config;

import com.ucv.lab12.controller.DocenteController;
import com.ucv.lab12.controller.DocenteFormController;
import com.ucv.lab12.controller.DeudaDocenteController;
import com.ucv.lab12.controller.DeudaDocenteFormController;
import com.ucv.lab12.repository.DeudaDocenteRepository;
import com.ucv.lab12.repository.DocenteRepository;
import com.ucv.lab12.repository.IDeudaDocenteRepository;
import com.ucv.lab12.repository.IDocenteRepository;
import com.ucv.lab12.service.DeudaDocenteService;
import com.ucv.lab12.service.DocenteService;
import com.ucv.lab12.service.IDeudaDocenteService;
import com.ucv.lab12.service.IDocenteService;

public class AppContext {
    private static AppContext instance;

    private final DatabaseConfig dbConfig;
    private final IDocenteService docenteService;
    private final IDeudaDocenteService deudaDocenteService;

    private AppContext() {
        this.dbConfig = new DatabaseConfig();
        IDocenteRepository docenteRepository = new DocenteRepository(dbConfig);
        IDeudaDocenteRepository deudaRepository = new DeudaDocenteRepository(dbConfig);
        this.docenteService = new DocenteService(docenteRepository);
        this.deudaDocenteService = new DeudaDocenteService(deudaRepository);
    }

    public static AppContext getInstance() {
        if (instance == null) instance = new AppContext();
        return instance;
    }

    public Object getController(Class<?> type) {
        if (type == DocenteController.class) return new DocenteController(docenteService);
        if (type == DocenteFormController.class) return new DocenteFormController(docenteService);
        if (type == DeudaDocenteController.class) return new DeudaDocenteController(deudaDocenteService);
        if (type == DeudaDocenteFormController.class) return new DeudaDocenteFormController(deudaDocenteService);

        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el controlador: " + type.getName(), e);
        }
    }

    public void destroy() {
        dbConfig.close();
    }
}
