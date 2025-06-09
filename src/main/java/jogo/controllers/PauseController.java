package jogo.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jogo.MainApplication;
import java.io.IOException;

public class PauseController {

    private Fase1Controller parentController;

    public void setParentController(Fase1Controller controller) {
        this.parentController = controller;
    }

    public void setPausePane(AnchorPane pane) {
        this.pausePane = pane;
    }

    @FXML
    private AnchorPane pausePane;

    @FXML
    private Button VoltarAoJogoButton;

    @FXML
    private Button MenuVoltarButton;

    @FXML
    private Button exitButton;

    @FXML
    private void handleVoltarAoJogoButtonClick(ActionEvent event) {
        if (parentController != null) {
            parentController.fecharPause();
        }
    }

    @FXML
    private void handleMenuVoltarButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu/menu-principal-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExitButtonAction(ActionEvent event) {
        Platform.exit();
    }
}
