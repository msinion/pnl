package com.ucv.lab12.controller;

import com.ucv.lab12.config.AppContext;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private void abrirDocentes() {
        abrirVentana("/com/ucv/lab12/docente-view.fxml", "PNL - Docentes");
    }

    @FXML
    private void abrirDeudas() {
        abrirVentana("/com/ucv/lab12/deuda-docente-view.fxml", "PNL - Deudas Administrativas");
    }

    @FXML
    private void salir() {
        Platform.exit();
    }

    private void abrirVentana(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            loader.setControllerFactory(AppContext.getInstance()::getController);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
