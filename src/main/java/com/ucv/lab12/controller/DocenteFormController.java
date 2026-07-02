package com.ucv.lab12.controller;

import com.ucv.lab12.model.Docente;
import com.ucv.lab12.service.IDocenteService;
import com.ucv.lab12.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DocenteFormController implements Initializable {
    @FXML private Label lblTitulo;
    @FXML private TextField txtNombres;
    @FXML private TextField txtDni;
    @FXML private TextField txtCelular;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> cboSituacion;
    @FXML private CheckBox chkActivo;
    @FXML private Label lblError;
    @FXML private Button btnCancelar;

    private final IDocenteService service;
    private Docente docente;
    private Runnable onGuardar;

    public DocenteFormController(IDocenteService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cboSituacion.getItems().setAll("Nombrado", "Contratado", "Cesante");
        cboSituacion.setValue("Nombrado");
        chkActivo.setSelected(true);
        lblError.setVisible(false);
        limitarLongitud(txtDni, 8);
        limitarLongitud(txtNombres, 120);
        limitarLongitud(txtCelular, 15);
        limitarLongitud(txtEmail, 100);
    }

    public void setDocente(Docente d) {
        this.docente = d;
        if (d != null) {
            lblTitulo.setText("Editar docente");
            txtNombres.setText(d.getNombres());
            txtDni.setText(d.getDni());
            txtCelular.setText(d.getCelular());
            txtEmail.setText(d.getEmail());
            cboSituacion.setValue(d.getSituacionLaboral());
            chkActivo.setSelected(d.isActivo());
        } else {
            lblTitulo.setText("Nuevo docente");
        }
    }

    public void setOnGuardar(Runnable onGuardar) {
        this.onGuardar = onGuardar;
    }

    @FXML
    private void onGuardar() {
        Docente d = docente != null ? docente : new Docente();
        d.setNombres(txtNombres.getText().trim());
        d.setDni(txtDni.getText().trim());
        d.setCelular(txtCelular.getText().trim());
        d.setEmail(txtEmail.getText().trim());
        d.setSituacionLaboral(cboSituacion.getValue());
        d.setActivo(chkActivo.isSelected());
        try {
            if (docente == null) service.crear(d); else service.actualizar(d);
            AlertUtil.info("Exito", "Docente guardado correctamente.");
            if (onGuardar != null) onGuardar.run();
            cerrar();
        } catch (IllegalArgumentException ex) {
            lblError.setText(ex.getMessage());
            lblError.setVisible(true);
        } catch (Exception ex) {
            AlertUtil.error("Error", "No se pudo guardar:\n" + ex.getMessage());
        }
    }

    @FXML private void onCancelar() { cerrar(); }

    private void cerrar() {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }

    private void limitarLongitud(TextInputControl control, int max) {
        control.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() > max) control.setText(oldVal);
        });
    }
}
