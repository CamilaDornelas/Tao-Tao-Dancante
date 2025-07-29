package jogo.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import jogo.servicos.GestorDePause;

import javafx.event.ActionEvent;


public class PauseController {

    private GestorDePause gestorDePause;

    public void setGestorDePause(GestorDePause gestorDePause) {
        this.gestorDePause = gestorDePause;
    }

    @FXML
    private AnchorPane pausePane;

    @FXML
    private void voltarAoJogo() {
        gestorDePause.voltar();
    }

    @FXML
    private void voltarAoMenu(ActionEvent event) {
        gestorDePause.voltarParaMenu(event);
    }

    @FXML
    private void sairDoJogo() {
        gestorDePause.sairDoJogo();
    }
}
