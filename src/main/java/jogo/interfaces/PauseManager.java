package jogo.interfaces;

import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import jogo.controllers.PauseController;

import javafx.event.ActionEvent;

import java.io.IOException;

public class PauseManager implements Pause {

    private final Pane rootPane;
    private AnchorPane pausePane;
    private boolean paused = false;
    private final Timeline animation;
    private final MediaPlayer mediaPlayer;


    public PauseManager(Pane rootPane, Timeline animation, MediaPlayer mediaPlayer) {
        this.rootPane = rootPane;
        this.animation = animation;
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void pause() {
        if (!paused) {
            animation.pause();
            mediaPlayer.pause();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pause/pause-view.fxml"));
                pausePane = loader.load();

                PauseController pauseController = loader.getController();
                pauseController.setPauseManager(this); // referência para fechar o pause e outras ações

                rootPane.getChildren().add(pausePane);
                paused = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resume() {
        if (paused) {
            animation.play();
            mediaPlayer.play();
            rootPane.getChildren().remove(pausePane);
            paused = false;
            rootPane.requestFocus();
        }
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void backToMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu/menu-principal-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exitGame() {
        System.exit(0);
    }
}
