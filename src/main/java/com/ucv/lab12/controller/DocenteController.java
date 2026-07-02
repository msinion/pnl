package com.ucv.lab12.controller;

import com.ucv.lab12.config.AppContext;
import com.ucv.lab12.model.Docente;
import com.ucv.lab12.service.IDocenteService;
import com.ucv.lab12.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DocenteController implements Initializable {
    @FXML private TextField txtNombres;
    @FXML private TextField txtDni;
    @FXML private Label lblTotal;
    @FXML private TableView<Docente> tableView;
    @FXML private TableColumn<Docente, Boolean> colSeleccion;
    @FXML private TableColumn<Docente, Integer> colId;
    @FXML private TableColumn<Docente, String> colNombres;
    @FXML private TableColumn<Docente, String> colDni;
    @FXML private TableColumn<Docente, String> colCelular;
    @FXML private TableColumn<Docente, String> colEmail;
    @FXML private TableColumn<Docente, String> colSituacion;
    @FXML private TableColumn<Docente, Boolean> colActivo;
    @FXML private TableColumn<Docente, Void> colAcciones;

    private final IDocenteService service;
    private final ObservableList<Docente> data = FXCollections.observableArrayList();

    public DocenteController(IDocenteService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarDatos("", "", false);
        txtNombres.setOnAction(e -> onBuscar());
        txtDni.setOnAction(e -> onBuscar());
    }

    private void configurarColumnas() {
        tableView.setEditable(true);
        colSeleccion.setCellValueFactory(cell -> cell.getValue().seleccionadoProperty());
        colSeleccion.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccion));
        colSeleccion.setEditable(true);
        colId.setCellValueFactory(new PropertyValueFactory<>("idDocente"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSituacion.setCellValueFactory(new PropertyValueFactory<>("situacionLaboral"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        colActivo.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item ? "Si" : "No");
            }
        });
        colAcciones.setCellFactory(crearCeldaAcciones());
        tableView.setItems(data);
    }

    private Callback<TableColumn<Docente, Void>, TableCell<Docente, Void>> crearCeldaAcciones() {
        return col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox hbox = new HBox(5, btnEditar, btnEliminar);
            {
                hbox.setAlignment(Pos.CENTER);
                btnEditar.setStyle("-fx-background-color:#00A896;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");
                btnEliminar.setStyle("-fx-background-color:#EF476F;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");
                btnEditar.setOnAction(e -> abrirFormulario(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> confirmarEliminar(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        };
    }

    private void cargarDatos(String nombres, String dni) {
        cargarDatos(nombres, dni, true);
    }

    private void cargarDatos(String nombres, String dni, boolean mostrarAlerta) {
        try {
            data.setAll(service.buscar(nombres, dni));
            lblTotal.setText("Total: " + data.size() + " docente(s)");
        } catch (Exception e) {
            data.clear();
            lblTotal.setText("No se pudo cargar docentes. Revise conexion o tablas.");
            if (mostrarAlerta) {
                AlertUtil.error("Error de conexion", "No se pudo cargar docentes:\n" + e.getMessage());
            }
        }
    }

    @FXML private void onBuscar() { cargarDatos(txtNombres.getText(), txtDni.getText()); }
    @FXML private void onCrear() { abrirFormulario(null); }

    @FXML
    private void onEliminarSeleccionados() {
        List<Integer> ids = data.stream().filter(Docente::isSeleccionado)
                .map(Docente::getIdDocente).collect(Collectors.toList());
        if (ids.isEmpty()) {
            AlertUtil.advertencia("Sin seleccion", "Marque al menos un docente.");
            return;
        }
        if (!AlertUtil.confirmar("Confirmar eliminacion", "¿Eliminar " + ids.size() + " docente(s)?")) return;
        service.eliminarSeleccionados(ids);
        onBuscar();
    }

    private void confirmarEliminar(Docente d) {
        if (!AlertUtil.confirmar("Confirmar eliminacion", "¿Eliminar a " + d.getNombres() + "?")) return;
        service.eliminar(d.getIdDocente());
        onBuscar();
    }

    private void abrirFormulario(Docente docente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/lab12/docente-form.fxml"));
            loader.setControllerFactory(AppContext.getInstance()::getController);
            Parent root = loader.load();
            DocenteFormController formCtrl = loader.getController();
            formCtrl.setDocente(docente);
            formCtrl.setOnGuardar(this::onBuscar);
            Stage modal = new Stage();
            modal.setTitle(docente == null ? "Nuevo docente" : "Editar docente");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", "No se pudo abrir el formulario:\n" + e.getMessage());
        }
    }
}
