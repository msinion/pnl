package com.ucv.lab12.controller;

import com.ucv.lab12.config.AppContext;
import com.ucv.lab12.model.DeudaDocente;
import com.ucv.lab12.service.IDeudaDocenteService;
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

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DeudaDocenteController implements Initializable {
    @FXML private TextField txtTipoDeuda;
    @FXML private TextField txtFecha;
    @FXML private TextField txtSituacion;
    @FXML private Label lblTotal;
    @FXML private TableView<DeudaDocente> tableView;
    @FXML private TableColumn<DeudaDocente, Boolean> colSeleccion;
    @FXML private TableColumn<DeudaDocente, Integer> colId;
    @FXML private TableColumn<DeudaDocente, String> colDni;
    @FXML private TableColumn<DeudaDocente, String> colDocente;
    @FXML private TableColumn<DeudaDocente, String> colTipo;
    @FXML private TableColumn<DeudaDocente, String> colFecha;
    @FXML private TableColumn<DeudaDocente, Double> colMonto;
    @FXML private TableColumn<DeudaDocente, String> colEstado;
    @FXML private TableColumn<DeudaDocente, String> colSituacion;
    @FXML private TableColumn<DeudaDocente, Void> colAcciones;

    private final IDeudaDocenteService service;
    private final ObservableList<DeudaDocente> data = FXCollections.observableArrayList();

    public DeudaDocenteController(IDeudaDocenteService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarDatos("", "", "", false);
        txtTipoDeuda.setOnAction(e -> onBuscar());
        txtFecha.setOnAction(e -> onBuscar());
        txtSituacion.setOnAction(e -> onBuscar());
    }

    private void configurarColumnas() {
        tableView.setEditable(true);
        colSeleccion.setCellValueFactory(cell -> cell.getValue().seleccionadoProperty());
        colSeleccion.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccion));
        colSeleccion.setEditable(true);
        colId.setCellValueFactory(new PropertyValueFactory<>("idDeuda"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dniDocente"));
        colDocente.setCellValueFactory(new PropertyValueFactory<>("nombresDocente"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoDeuda"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colSituacion.setCellValueFactory(new PropertyValueFactory<>("situacionLaboral"));
        colAcciones.setCellFactory(crearCeldaAcciones());
        tableView.setItems(data);
    }

    private Callback<TableColumn<DeudaDocente, Void>, TableCell<DeudaDocente, Void>> crearCeldaAcciones() {
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

    private void cargarDatos(String tipo, String fecha, String situacion) {
        cargarDatos(tipo, fecha, situacion, true);
    }

    private void cargarDatos(String tipo, String fecha, String situacion, boolean mostrarAlerta) {
        try {
            data.setAll(service.buscar(tipo, fecha, situacion));
            double total = data.stream().mapToDouble(DeudaDocente::getMonto).sum();
            lblTotal.setText("Total: " + data.size() + " deuda(s) | S/ " + String.format("%.2f", total));
        } catch (Exception e) {
            data.clear();
            lblTotal.setText("No se pudo cargar deudas. Revise conexion o tablas.");
            if (mostrarAlerta) {
                AlertUtil.error("Error de conexion", "No se pudo cargar las deudas:\n" + e.getMessage());
            }
        }
    }

    @FXML private void onBuscar() { cargarDatos(txtTipoDeuda.getText(), txtFecha.getText(), txtSituacion.getText()); }
    @FXML private void onCrear() { abrirFormulario(null); }

    @FXML
    private void onExportarPdf() {
        try (FileWriter writer = new FileWriter("reporte_deudas_docentes.pdf")) {
            writer.write("PNL - REPORTE DE DEUDAS ADMINISTRATIVAS\n");
            writer.write("DNI;DOCENTE;TIPO;FECHA;MONTO;ESTADO;SITUACION\n");
            for (DeudaDocente d : data) {
                writer.write(String.format("%s;%s;%s;%s;%.2f;%s;%s%n",
                        d.getDniDocente(), d.getNombresDocente(), d.getTipoDeuda(),
                        d.getFechaRegistro(), d.getMonto(), d.getEstado(), d.getSituacionLaboral()));
            }
            AlertUtil.info("Reporte generado", "Se genero reporte_deudas_docentes.pdf en la carpeta del proyecto.");
        } catch (IOException e) {
            AlertUtil.error("Error", "No se pudo exportar el reporte:\n" + e.getMessage());
        }
    }

    @FXML
    private void onEliminarSeleccionados() {
        List<Integer> ids = data.stream().filter(DeudaDocente::isSeleccionado)
                .map(DeudaDocente::getIdDeuda).collect(Collectors.toList());
        if (ids.isEmpty()) {
            AlertUtil.advertencia("Sin seleccion", "Marque al menos una deuda.");
            return;
        }
        if (!AlertUtil.confirmar("Confirmar eliminacion", "¿Eliminar " + ids.size() + " deuda(s)?")) return;
        service.eliminarSeleccionados(ids);
        onBuscar();
    }

    private void confirmarEliminar(DeudaDocente deuda) {
        if (!AlertUtil.confirmar("Confirmar eliminacion", "¿Eliminar deuda de " + deuda.getNombresDocente() + "?")) return;
        service.eliminar(deuda.getIdDeuda());
        onBuscar();
    }

    private void abrirFormulario(DeudaDocente deuda) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/lab12/deuda-docente-form.fxml"));
            loader.setControllerFactory(AppContext.getInstance()::getController);
            Parent root = loader.load();
            DeudaDocenteFormController formCtrl = loader.getController();
            formCtrl.setDeuda(deuda);
            formCtrl.setOnGuardar(this::onBuscar);
            Stage modal = new Stage();
            modal.setTitle(deuda == null ? "Nueva deuda docente" : "Editar deuda docente");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", "No se pudo abrir el formulario:\n" + e.getMessage());
        }
    }
}
