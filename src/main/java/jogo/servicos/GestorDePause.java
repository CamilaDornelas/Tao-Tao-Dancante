package jogo.servicos;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
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
import java.util.List;
import jogo.componentes.Setas;
import jogo.interfaces.Pause;

public class GestorDePause implements Pause {

    // Instância única (Singleton)
    private static GestorDePause instance;

    private final Pane rootPane;
    private AnchorPane telaPause;
    private boolean pausado = false;
    private final Timeline animacoes;
    private final MediaPlayer audio;
    private final Runnable voltarSetas;
    private final GerenciadorSetas gerenciadorSetas;

    // Construtor privado (ninguém fora pode instanciar diretamente)
    private GestorDePause(Pane rootPane, Timeline animacoes, MediaPlayer audio,
                          List<Setas> activeArrows, Runnable resumeGameAction,
                          GerenciadorSetas gerenciadorSetas) {
        this.rootPane = rootPane;
        this.animacoes = animacoes;
        this.audio = audio;
        this.voltarSetas = resumeGameAction;
        this.gerenciadorSetas = gerenciadorSetas;
    }

    // Metodo para obter a instância única
    public static GestorDePause getInstance(Pane rootPane, Timeline animacoes, MediaPlayer audio,
                                            List<Setas> activeArrows, Runnable resumeGameAction,
                                            GerenciadorSetas gerenciadorSetas) {
        if (instance == null) {
            instance = new GestorDePause(rootPane, animacoes, audio, activeArrows, resumeGameAction, gerenciadorSetas);
        }
        return instance;
    }

    @Override
    public void pause() {
        if (!pausado) {
            animacoes.pause();
            audio.pause();

            if (gerenciadorSetas != null) {
                gerenciadorSetas.pauseSpawn();
                gerenciadorSetas.setJogoPausado(true);
            }

            for (Setas arrow : gerenciadorSetas.getSetasAtivas()) {
                ParallelTransition riseAnimation = arrow.getRiseAnimation();
                if (riseAnimation != null && riseAnimation.getStatus() == Animation.Status.RUNNING) {
                    riseAnimation.pause();
                }
            }

            try {
                FXMLLoader pause = new FXMLLoader(getClass().getResource("/pause/pause-view.fxml"));
                telaPause = pause.load();

                PauseController pauseController = pause.getController();
                pauseController.setGestorDePause(this);
                pauseController.setMediaPlayer(audio); // ✨ NOVO: Passa o MediaPlayer para o controle de volume

                rootPane.getChildren().add(telaPause);
                pausado = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void voltar() {
        if (!pausado) return;

        animacoes.play();
        audio.play();

        if (gerenciadorSetas != null) {
            gerenciadorSetas.resumeSpawn();
            gerenciadorSetas.setJogoPausado(false);
        }

        for (Setas arrow : gerenciadorSetas.getSetasAtivas()) {
            ParallelTransition riseAnimation = arrow.getRiseAnimation();
            if (riseAnimation != null && riseAnimation.getStatus() == Animation.Status.PAUSED) {
                riseAnimation.play();
            }
        }

        rootPane.getChildren().remove(telaPause);
        pausado = false;
        rootPane.requestFocus();
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
