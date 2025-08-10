package jogo.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;


public class Transicao1Controller {

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
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FaseController faseController = new FaseController();
            faseController.carregarFase(stage, 1); // Carrega fase 1
            
        } catch (Exception erro) {
            System.err.println("❌ Erro ao carregar fase: " + erro.getMessage());
            erro.printStackTrace();
        }
    }
}