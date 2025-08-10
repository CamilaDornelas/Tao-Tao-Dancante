package jogo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import principal.MainApplication;

public class MenuController {

    @FXML
    private Button comecarBotao;

    @FXML
    private Button sairBotao;

//    @FXML
//    private void comecar(ActionEvent event) {
//        try {
//            MainApplication.showTransicao1Screen();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    @FXML
    private void comecar(ActionEvent event) {
        System.out.println("Começar clicado!");

        MainApplication.trocarTela("/transicao1/transicao1-view.fxml");
    }


    @FXML
    private void sairDoJogo(ActionEvent event) {
        Platform.exit();
    }
}