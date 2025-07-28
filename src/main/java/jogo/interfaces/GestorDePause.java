package jogo.interfaces;

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
import jogo.componentes.Setas; // Certifique-se de que esta importação existe

public class GestorDePause implements Pause {

    private final Pane rootPane;
    private AnchorPane telaPause;
    private boolean pausado = false;
    private final Timeline animacoes;
    private final MediaPlayer audio;
    private final List<Setas> setasAtivas;
    private final Runnable voltarSetas; // Certifique-se de que o nome é este ou 'voltarSetas'

    public GestorDePause(Pane rootPane, Timeline animacoes, MediaPlayer audio, List<Setas> activeArrows, Runnable resumeGameAction) {
        this.rootPane = rootPane;
        this.animacoes = animacoes;
        this.audio = audio;
        this.setasAtivas = activeArrows;
        this.voltarSetas = resumeGameAction; // Ou 'this.voltarSetas = voltarSetas;' se você usou esse nome
    }

    public List<Setas> getSetasAtivas() {
        return setasAtivas;
    }


    @Override
    public void pause() {
        if (!pausado) {
            animacoes.pause();
            audio.pause();
            for (Setas arrow : setasAtivas) {
                ParallelTransition riseAnimation = arrow.getRiseAnimation();
                if (riseAnimation != null && riseAnimation.getStatus() == javafx.animation.Animation.Status.RUNNING) {
                    riseAnimation.pause();
                }
            }
            try {
                FXMLLoader pause = new FXMLLoader(getClass().getResource("/pause/pause-view.fxml"));
                telaPause = pause.load();

                PauseController pauseController = pause.getController();
                pauseController.setGestorDePause(this);

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
            for (Setas arrow : setasAtivas) {
                ParallelTransition riseAnimation = arrow.getRiseAnimation();
                if (riseAnimation != null && riseAnimation.getStatus() == javafx.animation.Animation.Status.PAUSED) {
                    riseAnimation.play();
                }
            }
            rootPane.getChildren().remove(telaPause);
            pausado = false;
            rootPane.requestFocus();

            // *** AQUI É ONDE VOCÊ DEVE TER COMETIDO O ERRO ***
            // O CORRETO É CHAMAR .run(), NÃO ITERAR!
            if (voltarSetas != null) { // Ou 'if (voltarSetas != null)'
                voltarSetas.run();    // Ou 'voltarSetas.run();'
            }
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