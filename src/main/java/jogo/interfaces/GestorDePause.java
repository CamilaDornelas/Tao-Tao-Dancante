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


public class GestorDePause implements Pause {

    private final Pane rootPane;
    private AnchorPane telaPause;
    private boolean pausado = false;
    private final Timeline animacoes;
    private final MediaPlayer audio;


    public GestorDePause(Pane rootPane, Timeline animacoes, MediaPlayer audio) {
        this.rootPane = rootPane;
        this.animacoes = animacoes;
        this.audio = audio;
    }

    @Override
    public void pause() {
        if (!pausado) {
            animacoes.pause();
            audio.pause();
            try {
                FXMLLoader pause = new FXMLLoader(getClass().getResource("/pause/pause-view.fxml"));
                telaPause = pause.load();

                PauseController pauseController = pause.getController();
                pauseController.setGestorDePause(this); // referência para fechar o pause e outras ações

                rootPane.getChildren().add(telaPause);
                pausado = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void voltar() {
        if (pausado) {
            animacoes.play();
            audio.play();
            rootPane.getChildren().remove(telaPause);
            pausado = false;
            rootPane.requestFocus();
        }
    }

    @Override
    public boolean estaPausado() {
        return pausado;
    }

    @Override
    public void voltarParaMenu(ActionEvent event) {
        try {
            FXMLLoader menu = new FXMLLoader(getClass().getResource("/menu/menu-principal-view.fxml"));
            Parent root = menu.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException erro) {
            erro.printStackTrace();
        }
    }

    @Override
    public void sairDoJogo() {
        System.exit(0);
    }
}
