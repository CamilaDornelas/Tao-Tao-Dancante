package jogo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class VitoriaController {

    @FXML
    private void menuVoltar(ActionEvent event) {
        try {
            FXMLLoader menu = new FXMLLoader(getClass().getResource("/menu/menu-principal-view.fxml"));
            Parent root = menu.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            root.requestFocus();
        } catch (IOException erro) {
            erro.printStackTrace();
        }
    }
}