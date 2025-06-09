package jogo.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import jogo.personagens.BardoDanca;

import java.io.IOException;

public class Fase1Controller {

    private boolean pauseAtivo = false;
    private AnchorPane pausePane;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private void initialize() {
        // Cria o ogro que dança
        BardoDanca bardo = new BardoDanca(282, 415);
        bardo.setLayoutX(820); // ajuste conforme o layout
        bardo.setLayoutY(270);
        rootPane.getChildren().add(bardo);


        //TUDO QUE FOR REFERENTE AO PAUSE, EU NAO SE SE ELE ESTÁ PAUSANDO TUDO QUE ESTÁ ACONTECENDO NA TELA
        //QUANDO FOR IMPLEMENTAR A LOGICA DAS SETAS E DA MUSICA, TEM QUE VERIFICAR ISSO ^

        // Garante que o rootPane possa receber foco
        rootPane.setFocusTraversable(true);

        // Foca o rootPane após carregar
        Platform.runLater(() -> rootPane.requestFocus());

        // Adiciona escuta da tecla ESC
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if(!pauseAtivo) {
                    mostrarPause();
                } else {
                fecharPause();
            }}
        });
    }

    private void mostrarPause() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pause/pause-view.fxml"));
            pausePane = loader.load();

            PauseController pauseController = loader.getController();
            pauseController.setParentController(this); // Comunicação com o controller principal
            pauseController.setPausePane(pausePane);   // Referência para poder fechar

            rootPane.getChildren().add(pausePane);
            pauseAtivo = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fecharPause() {
        if (pausePane != null) {
            rootPane.getChildren().remove(pausePane);
            pauseAtivo = false;

            Platform.runLater(() -> rootPane.requestFocus());
        }
    }
}
