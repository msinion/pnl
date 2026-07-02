package com.ucv.lab12.model;

import javafx.beans.property.*;

public class Docente {
    private final IntegerProperty idDocente = new SimpleIntegerProperty();
    private final StringProperty nombres = new SimpleStringProperty();
    private final StringProperty dni = new SimpleStringProperty();
    private final StringProperty celular = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty situacionLaboral = new SimpleStringProperty();
    private final BooleanProperty activo = new SimpleBooleanProperty(true);
    private final BooleanProperty seleccionado = new SimpleBooleanProperty(false);

    public Docente() {}

    public Docente(int idDocente, String nombres, String dni, String celular,
                   String email, String situacionLaboral, boolean activo) {
        setIdDocente(idDocente);
        setNombres(nombres);
        setDni(dni);
        setCelular(celular);
        setEmail(email);
        setSituacionLaboral(situacionLaboral);
        setActivo(activo);
    }

    public IntegerProperty idDocenteProperty() { return idDocente; }
    public StringProperty nombresProperty() { return nombres; }
    public StringProperty dniProperty() { return dni; }
    public StringProperty celularProperty() { return celular; }
    public StringProperty emailProperty() { return email; }
    public StringProperty situacionLaboralProperty() { return situacionLaboral; }
    public BooleanProperty activoProperty() { return activo; }
    public BooleanProperty seleccionadoProperty() { return seleccionado; }

    public int getIdDocente() { return idDocente.get(); }
    public String getNombres() { return nombres.get(); }
    public String getDni() { return dni.get(); }
    public String getCelular() { return celular.get(); }
    public String getEmail() { return email.get(); }
    public String getSituacionLaboral() { return situacionLaboral.get(); }
    public boolean isActivo() { return activo.get(); }
    public boolean isSeleccionado() { return seleccionado.get(); }

    public void setIdDocente(int v) { idDocente.set(v); }
    public void setNombres(String v) { nombres.set(v); }
    public void setDni(String v) { dni.set(v); }
    public void setCelular(String v) { celular.set(v); }
    public void setEmail(String v) { email.set(v); }
    public void setSituacionLaboral(String v) { situacionLaboral.set(v); }
    public void setActivo(boolean v) { activo.set(v); }
    public void setSeleccionado(boolean v) { seleccionado.set(v); }
}
