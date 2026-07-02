package com.ucv.lab12.controller;

import com.ucv.lab12.model.DeudaDocente;
import com.ucv.lab12.service.IDeudaDocenteService;
import com.ucv.lab12.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DeudaDocenteFormController implements Initializable {
    @FXML private Label lblTitulo;
    @FXML private TextField txtDni;
    @FXML private TextField txtDocente;
    @FXML private TextField txtTipo;
    @FXML private TextField txtFecha;
    @FXML private TextField txtMonto;
    @FXML private ComboBox<String> cboEstado;
    @FXML private ComboBox<String> cboSituacion;
    @FXML private TextArea txtObservacion;
    @FXML private Label lblError;
    @FXML private Button btnCancelar;

    private final IDeudaDocenteService service;
    private DeudaDocente deuda;
    private Runnable onGuardar;

    public DeudaDocenteFormController(IDeudaDocenteService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cboEstado.getItems().setAll("Pendiente", "En convenio", "Pagada", "Observada");
        cboSituacion.getItems().setAll("Nombrado", "Contratado", "Cesante");
        cboEstado.setValue("Pendiente");
        cboSituacion.setValue("Nombrado");
        txtFecha.setText(LocalDate.now().toString());
        lblError.setVisible(false);
        limitarLongitud(txtDni, 8);
        limitarLongitud(txtDocente, 120);
        limitarLongitud(txtTipo, 80);
        limitarLongitud(txtObservacion, 250);
    }

    public void setDeuda(DeudaDocente d) {
        this.deuda = d;
        if (d != null) {
            lblTitulo.setText("Editar deuda docente");
            txtDni.setText(d.getDniDocente());
            txtDocente.setText(d.getNombresDocente());
            txtTipo.setText(d.getTipoDeuda());
            txtFecha.setText(d.getFechaRegistro());
            txtMonto.setText(String.valueOf(d.getMonto()));
            cboEstado.setValue(d.getEstado());
            cboSituacion.setValue(d.getSituacionLaboral());
            txtObservacion.setText(d.getObservacion());
        } else {
            lblTitulo.setText("Nueva deuda docente");
        }
    }

    public void setOnGuardar(Runnable onGuardar) {
        this.onGuardar = onGuardar;
    }

    @FXML
    private void onGuardar() {
        DeudaDocente d = deuda != null ? deuda : new DeudaDocente();
        try {
            d.setDniDocente(txtDni.getText().trim());
            d.setNombresDocente(txtDocente.getText().trim());
            d.setTipoDeuda(txtTipo.getText().trim());
            d.setFechaRegistro(txtFecha.getText().trim());
            d.setMonto(Double.parseDouble(txtMonto.getText().trim()));
            d.setEstado(cboEstado.getValue());
            d.setSituacionLaboral(cboSituacion.getValue());
            d.setObservacion(txtObservacion.getText().trim());

            if (deuda == null) service.crear(d); else service.actualizar(d);
            AlertUtil.info("Exito", "Deuda guardada correctamente.");
            if (onGuardar != null) onGuardar.run();
            cerrar();
        } catch (NumberFormatException ex) {
            mostrarError("El monto debe ser numerico.");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            AlertUtil.error("Error", "No se pudo guardar:\n" + ex.getMessage());
        }
    }

    @FXML private void onCancelar() { cerrar(); }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }

    private void cerrar() {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }

    private void limitarLongitud(TextInputControl control, int max) {
        control.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() > max) control.setText(oldVal);
        });
    }
}
