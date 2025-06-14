package jogo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;


public class Transicao1Controller {

    @FXML
    private Button menuVoltarButton;
    @FXML
    private Button comecarButton;

    @FXML
    private void voltarMenu(ActionEvent event) {
        try {
            FXMLLoader menu = new FXMLLoader(getClass().getResource("/menu/menu-principal-view.fxml"));
            Parent root = menu.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException erro) {
            erro.printStackTrace();
        }
    }
    @FXML
    private void comecarJogo(ActionEvent event) {
        try {
            FXMLLoader fase1 = new FXMLLoader(getClass().getResource("/fase1/fase1-view.fxml"));
            Parent root = fase1.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException erro) {
            erro.printStackTrace();
        }
    }
}