package jogo.interfaces;

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
import jogo.servicos.GerenciadorSetas;

public class GestorDePause implements Pause {

    private final Pane rootPane;
    private AnchorPane telaPause;
    private boolean pausado = false;
    private final Timeline animacoes; // Representa a animação do Bardo
    private final MediaPlayer audio;
    //private final List<Setas> setasAtivas;
    private final Runnable voltarSetas; // Reavaliar uso conforme explicado antes
    private final GerenciadorSetas gerenciadorSetas; // Referência ao GerenciadorSetas

    // CONSTRUTOR CORRIGIDO: Adiciona 'gerenciadorSetas' como parâmetro
    public GestorDePause(Pane rootPane, Timeline animacoes, MediaPlayer audio, List<Setas> activeArrows, Runnable resumeGameAction, GerenciadorSetas gerenciadorSetas) {
        this.rootPane = rootPane;
        this.animacoes = animacoes;
        this.audio = audio;
        //this.setasAtivas = activeArrows;
        this.voltarSetas = resumeGameAction; // Callback para retomar o jogo (pode ser null se não for mais necessário)
        this.gerenciadorSetas = gerenciadorSetas; // Inicializa a referência ao GerenciadorSetas
    }

    @Override
    public void pause() {
        if (!pausado) {
            animacoes.pause(); // Pausa a animação do Bardo
            audio.pause(); // Pausa o áudio

            // O QUE FOI ADICIONADO: Pausar o spawn de novas setas
            if (gerenciadorSetas != null) {
                gerenciadorSetas.pauseSpawn();
                gerenciadorSetas.setJogoPausado(true);
            }

            // Pausar as animações individuais das setas ativas
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

                rootPane.getChildren().add(telaPause);
                pausado = true; // Define o estado como pausado
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

        rootPane.getChildren().remove(telaPause); // Remove a tela de pause
        pausado = false; // Define o estado como não pausado
        rootPane.requestFocus(); // Devolve o foco para a tela principal

        // Reavaliar esta parte:
        // Se a retomada do spawn de setas já é feita por gerenciadorSetas.resumeSpawn(),
        // e se 'voltarSetas' chama gerenciadorDeSetas::iniciar (com 3 segundos de atraso),
        // esta linha pode ser removida para uma retomada mais suave e imediata do jogo.
        // if (voltarSetas != null) {
        //     voltarSetas.run();
        // }
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