package jogo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import jogo.MainApplication;

import java.io.IOException;

public class MenuController extends IOException {

    @FXML
    private Button startButton;

    @FXML
    private Button exitButton;

    @FXML
    private void handleStartButtonAction(ActionEvent event) {
        try {
            MainApplication.showTransicao1Screen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExitButtonAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void initialize() {
        startButton.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-border-color: transparent;");
        exitButton.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-border-color: transparent;");

        startButton.setText("");
        exitButton.setText("");
    }
}