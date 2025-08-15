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

    private static GestorDePause instancia;

    private static final String CAMINHO_TELA_PAUSE = "/pause/pause-view.fxml";
    private static final String CAMINHO_MENU_PRINCIPAL = "/menu/menu-principal-view.fxml";

    private final Pane painelPrincipal;
    private AnchorPane telaPause;
    private boolean jogoPausado = false;
    private final Timeline animacoes;
    private final MediaPlayer reprodutorMidia;
    private final Runnable acaoRetornarJogo;
    private final GerenciadorSetas gerenciadorSetas;

    private GestorDePause(Pane painelPrincipal, Timeline animacoes, MediaPlayer reprodutorMidia,
                          List<Setas> setasAtivas, Runnable acaoRetornarJogo,
                          GerenciadorSetas gerenciadorSetas) {
        this.painelPrincipal = painelPrincipal;
        this.animacoes = animacoes;
        this.reprodutorMidia = reprodutorMidia;
        this.acaoRetornarJogo = acaoRetornarJogo;
        this.gerenciadorSetas = gerenciadorSetas;
    }

    public static GestorDePause getInstance(Pane painelPrincipal, Timeline animacoes, MediaPlayer reprodutorMidia,
                                            List<Setas> setasAtivas, Runnable acaoRetornarJogo,
                                            GerenciadorSetas gerenciadorSetas) {
        if (instancia == null) {
            instancia = new GestorDePause(painelPrincipal, animacoes, reprodutorMidia, setasAtivas, acaoRetornarJogo, gerenciadorSetas);
        }
        return instancia;
    }

    @Override
    public void pause() {
        if (!jogoPausado) {
            pausarAnimacoesEAudio();
            pausarGerenciadorSetas();
            pausarAnimacoesDasSetasAtivas();
            exibirTelaDePause();
            jogoPausado = true;
        }
    }

    private void pausarAnimacoesEAudio() {
        if (animacoes != null) {
            animacoes.pause();
        }
        if (reprodutorMidia != null) {
            reprodutorMidia.pause();
        }
    }

    private void pausarGerenciadorSetas() {
        if (gerenciadorSetas != null) {
            gerenciadorSetas.pauseSpawn();
            gerenciadorSetas.setJogoPausado(true);
        }
    }

    private void pausarAnimacoesDasSetasAtivas() {
        if (gerenciadorSetas != null && gerenciadorSetas.getSetasAtivas() != null) {
            for (Setas seta : gerenciadorSetas.getSetasAtivas()) {
                ParallelTransition animacaoSubida = seta.getAnimacaoSubida();
                if (animacaoSubida != null && animacaoSubida.getStatus() == Animation.Status.RUNNING) {
                    animacaoSubida.pause();
                }
            }
        }
    }

    private void exibirTelaDePause() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(CAMINHO_TELA_PAUSE));
            telaPause = fxmlLoader.load();

            PauseController pauseController = fxmlLoader.getController();
            pauseController.setGestorDePause(this);
            pauseController.setReprodutorMidia(reprodutorMidia);

            painelPrincipal.getChildren().add(telaPause);
        } catch (IOException e) {
            System.err.println("❌ Erro ao carregar a tela de pause: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void voltar() {
        if (!jogoPausado) return;

        retomarAnimacoesEAudio();
        retomarGerenciadorSetas();
        retomarAnimacoesDasSetasAtivas();
        removerTelaDePause();

        jogoPausado = false;
        painelPrincipal.requestFocus();
    }

    private void retomarAnimacoesEAudio() {
        if (animacoes != null) {
            animacoes.play();
        }
        if (reprodutorMidia != null) {
            reprodutorMidia.play();
        }
    }

    private void retomarGerenciadorSetas() {
        if (gerenciadorSetas != null) {
            gerenciadorSetas.resumeSpawn();
            gerenciadorSetas.setJogoPausado(false);
        }
    }

    private void retomarAnimacoesDasSetasAtivas() {
        if (gerenciadorSetas != null && gerenciadorSetas.getSetasAtivas() != null) {
            for (Setas seta : gerenciadorSetas.getSetasAtivas()) {
                ParallelTransition animacaoSubida = seta.getAnimacaoSubida();
                if (animacaoSubida != null && animacaoSubida.getStatus() == Animation.Status.PAUSED) {
                    animacaoSubida.play();
                }
            }
        }
    }

    private void removerTelaDePause() {
        if (telaPause != null) {
            painelPrincipal.getChildren().remove(telaPause);
        }
    }

    @Override
    public boolean estaPausado() {
        return jogoPausado;
    }

    @Override
    public void voltarParaMenu(ActionEvent evento) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(CAMINHO_MENU_PRINCIPAL));
            Parent root = fxmlLoader.load();

            Stage palco = (Stage) ((Node) evento.getSource()).getScene().getWindow();
            palco.setScene(new Scene(root));
            palco.show();
        } catch (IOException erro) {
            System.err.println("❌ Erro ao carregar a tela do menu principal: " + erro.getMessage());
            erro.printStackTrace();
        }
    }

    @Override
    public void sairDoJogo() {
        System.exit(0);
    }
}