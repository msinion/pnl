package com.ucv.lab12.model;

import javafx.beans.property.*;

public class DeudaDocente {
    private final IntegerProperty idDeuda = new SimpleIntegerProperty();
    private final StringProperty dniDocente = new SimpleStringProperty();
    private final StringProperty nombresDocente = new SimpleStringProperty();
    private final StringProperty tipoDeuda = new SimpleStringProperty();
    private final StringProperty fechaRegistro = new SimpleStringProperty();
    private final DoubleProperty monto = new SimpleDoubleProperty();
    private final StringProperty estado = new SimpleStringProperty();
    private final StringProperty situacionLaboral = new SimpleStringProperty();
    private final StringProperty observacion = new SimpleStringProperty();
    private final BooleanProperty seleccionado = new SimpleBooleanProperty(false);

    public DeudaDocente() {}

    public DeudaDocente(int idDeuda, String dniDocente, String nombresDocente,
                        String tipoDeuda, String fechaRegistro, double monto,
                        String estado, String situacionLaboral, String observacion) {
        setIdDeuda(idDeuda);
        setDniDocente(dniDocente);
        setNombresDocente(nombresDocente);
        setTipoDeuda(tipoDeuda);
        setFechaRegistro(fechaRegistro);
        setMonto(monto);
        setEstado(estado);
        setSituacionLaboral(situacionLaboral);
        setObservacion(observacion);
    }

    public IntegerProperty idDeudaProperty() { return idDeuda; }
    public StringProperty dniDocenteProperty() { return dniDocente; }
    public StringProperty nombresDocenteProperty() { return nombresDocente; }
    public StringProperty tipoDeudaProperty() { return tipoDeuda; }
    public StringProperty fechaRegistroProperty() { return fechaRegistro; }
    public DoubleProperty montoProperty() { return monto; }
    public StringProperty estadoProperty() { return estado; }
    public StringProperty situacionLaboralProperty() { return situacionLaboral; }
    public StringProperty observacionProperty() { return observacion; }
    public BooleanProperty seleccionadoProperty() { return seleccionado; }

    public int getIdDeuda() { return idDeuda.get(); }
    public String getDniDocente() { return dniDocente.get(); }
    public String getNombresDocente() { return nombresDocente.get(); }
    public String getTipoDeuda() { return tipoDeuda.get(); }
    public String getFechaRegistro() { return fechaRegistro.get(); }
    public double getMonto() { return monto.get(); }
    public String getEstado() { return estado.get(); }
    public String getSituacionLaboral() { return situacionLaboral.get(); }
    public String getObservacion() { return observacion.get(); }
    public boolean isSeleccionado() { return seleccionado.get(); }

    public void setIdDeuda(int v) { idDeuda.set(v); }
    public void setDniDocente(String v) { dniDocente.set(v); }
    public void setNombresDocente(String v) { nombresDocente.set(v); }
    public void setTipoDeuda(String v) { tipoDeuda.set(v); }
    public void setFechaRegistro(String v) { fechaRegistro.set(v); }
    public void setMonto(double v) { monto.set(v); }
    public void setEstado(String v) { estado.set(v); }
    public void setSituacionLaboral(String v) { situacionLaboral.set(v); }
    public void setObservacion(String v) { observacion.set(v); }
    public void setSeleccionado(boolean v) { seleccionado.set(v); }
}
