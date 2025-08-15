package jogo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import principal.MainApplication;

public class MenuController {

    @FXML
    private Button botaoComecar;

    @FXML
    private Button botaoSair;

    @FXML
    private void aoPressionarComecar(ActionEvent evento) {
        System.out.println("Botão Começar pressionado!");
        MainApplication.trocarTela("/transicao1/transicao1-view.fxml");
    }

    @FXML
    private void aoPressionarSair(ActionEvent evento) {
        Platform.exit();
    }
}