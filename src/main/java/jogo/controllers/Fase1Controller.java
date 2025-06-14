package jogo.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import jogo.interfaces.Pause;
import jogo.personagens.BardoDanca;
import jogo.personagens.Lorde;
import jogo.interfaces.GestorDePause;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.animation.PauseTransition;


public class Fase1Controller {

    private Pause GestorDePause;

    @FXML
    private AnchorPane telaFase1;

    @FXML
    private void initialize() {

        // Cria o ogro que dança
        BardoDanca bardo = new BardoDanca(282, 415);
        bardo.setLayoutX(890); // ajuste conforme o layout
        bardo.setLayoutY(335);
        telaFase1.getChildren().add(bardo);

        // Cria o lorde
        Lorde lorde = new Lorde(210, 380);
        lorde.setLayoutX(120);
        lorde.setLayoutY(370);
        telaFase1.getChildren().add(lorde);

        //MUSICA
        String musica = getClass().getResource("/assets/musica/song1.mp3").toExternalForm();
        Media midia = new Media(musica);
        MediaPlayer audio = new MediaPlayer(midia);


        // Cria um timer de 3 segundos
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            audio.play();
        });
        delay.play();


        //TUDO AQUI É PRO PAUSE
        GestorDePause = new GestorDePause(telaFase1, bardo.getAnimacao(), audio);

        telaFase1.setFocusTraversable(true);
        Platform.runLater(() -> telaFase1.requestFocus());

        telaFase1.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (!GestorDePause.estaPausado()) {
                    GestorDePause.pause();
                } else {
                    GestorDePause.voltar();
                }
            }
        });

    }
}