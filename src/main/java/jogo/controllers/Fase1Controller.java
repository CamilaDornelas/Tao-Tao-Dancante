package jogo.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import jogo.personagens.BardoDanca;
import jogo.personagens.Lorde;

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
        bardo.setLayoutX(890); // ajuste conforme o layout
        bardo.setLayoutY(335);
        rootPane.getChildren().add(bardo);


        // Cria o lorde
        Lorde lorde = new Lorde(210, 380);
        lorde.setLayoutX(120);
        lorde.setLayoutY(370);
        rootPane.getChildren().add(lorde);



        //TUDO QUE FOR REFERENTE AO PAUSE, EU NAO SE SE ELE ESTÁ PAUSANDO TUDO QUE ESTÁ ACONTECENDO NA TELA
        //QUANDO FOR IMPLEMENTAR A LOGICA DAS SETAS E DA MUSICA, TEM QUE VERIFICAR ISSO ^

        // Garante que o rootPane possa receber foco do teclado
        rootPane.setFocusTraversable(true);

        // Foca o rootPane após carregar, ou seja, agora ele vai escutar as teclas
        Platform.runLater(() -> rootPane.requestFocus());

        // Adiciona escuta da tecla ESC
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) { //Quando alguém pressionar qualquer tecla no teclado, verifica se foi a tecla ESC
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
            pausePane = loader.load(); //colocando o FXML na variavel pausePane

            PauseController pauseController = loader.getController(); //pegando os botoes do controller pause
            pauseController.setParentController(this); // adicionando os botoes do controller pause aqui
            pauseController.setPausePane(pausePane);   // Referência para poder fechar

            rootPane.getChildren().add(pausePane); //adicionando o menu de pause dentro do rootPane (tela)
            pauseAtivo = true;

        } catch (IOException e) { //caso não consiga carregar o arquivo FXML
            e.printStackTrace();
        }
    }

    public void fecharPause() {
        if (pausePane != null) { //verifica se pausePane existe.
            rootPane.getChildren().remove(pausePane); //remove o pause da tela
            pauseAtivo = false; //desativa

            Platform.runLater(() -> rootPane.requestFocus()); //depois de fechar o pause, ele devolve o foco pro rootPane
        }
    }
}
