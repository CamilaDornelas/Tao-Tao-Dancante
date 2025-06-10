package jogo.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import jogo.personagens.BardoDanca;
import jogo.personagens.Lorde;
import jogo.interfaces.PauseManager;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.animation.PauseTransition;




public class Fase1Controller {

    private PauseManager pauseManager;

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

        //MUSICA
        String musica = getClass().getResource("/assets/musica/song1.mp3").toExternalForm();
        Media media = new Media(musica);
        MediaPlayer mediaPlayer = new MediaPlayer(media);


        // Cria um timer de 3 segundos
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            mediaPlayer.play();
        });
        delay.play();


        //TUDO AQUI É PRO PAUSE
        pauseManager = new PauseManager(rootPane, bardo.getAnimacao(), mediaPlayer);

        rootPane.setFocusTraversable(true);
        Platform.runLater(() -> rootPane.requestFocus());

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (!pauseManager.isPaused()) {
                    pauseManager.pause();
                } else {
                    pauseManager.resume();
                }
            }
        });

    }
}