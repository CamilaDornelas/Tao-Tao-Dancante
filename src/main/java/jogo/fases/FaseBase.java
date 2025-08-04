package jogo.fases;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import jogo.componentes.PlacarDeVida;
import jogo.interfaces.Pause;
import jogo.personagens.BardoDanca;
import jogo.personagens.Lorde;
import jogo.servicos.GerenciadorSetas;
import jogo.servicos.GestorDePause;
import jogo.servicos.FinalizarFase;
import javafx.stage.Stage;

public abstract class FaseBase {

    @FXML protected AnchorPane telaFase;
    @FXML protected ImageView background;

    protected MediaPlayer audio;
    protected Rectangle hitZone;
    protected PlacarDeVida placarDeVida;
    protected Lorde lorde;
    protected BardoDanca bardo;
    protected GerenciadorSetas gerenciadorDeSetas;
    protected GestorDePause gestorDePause;

    protected boolean jogoTerminou = false;
    protected double pontuacao = 0.5;

    protected final double GANHO_POR_ACERTO = 0.03;
    protected final double PENALIDADE_POR_ERRO = 0.06;

    protected ImageView erroImageView;

    @FXML
    protected void initialize() {
        inicializarBackground();
        inicializarPersonagens();
        inicializarHitZone();
        inicializarPlacar();
        inicializarMusica();
        inicializarGerenciadorDeSetas();
        configurarTeclado();
        iniciarFase();
    }

    protected abstract void inicializarBackground();
    protected abstract void inicializarPersonagens();
    protected abstract void inicializarMusica();
    protected abstract void iniciarFase();

    protected void definirBackground(String caminhoRelativo) {
        Image img = new Image(getClass().getResourceAsStream(caminhoRelativo));
        background.setImage(img);
    }

    protected void inicializarHitZone() {
        hitZone = new Rectangle(300, 15, (3 * 165) + 190, 130);
        hitZone.setFill(Color.TRANSPARENT);
        hitZone.setStroke(Color.TRANSPARENT);
        hitZone.setStrokeWidth(3);
        telaFase.getChildren().add(hitZone);
    }

    protected void inicializarPlacar() {
        placarDeVida = new PlacarDeVida();
        placarDeVida.setLayoutX(400);
        placarDeVida.setLayoutY(700);
        telaFase.getChildren().add(placarDeVida);
    }

    protected void inicializarGerenciadorDeSetas() {
        gerenciadorDeSetas = new GerenciadorSetas(telaFase, hitZone, audio, placarDeVida, this::verificarResultadoFinal);
        gerenciadorDeSetas.setErroAction(this::showErrorImage);
        gestorDePause = new GestorDePause(telaFase, bardo.getAnimacao(), audio, gerenciadorDeSetas.getSetasAtivas(), gerenciadorDeSetas::spawnRandomArrow, gerenciadorDeSetas);
    }

    protected void configurarTeclado() {
        telaFase.setFocusTraversable(true);
        Platform.runLater(() -> telaFase.requestFocus());

        telaFase.setOnKeyPressed(event -> {
            if (!jogoTerminou) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    if (!gestorDePause.estaPausado()) {
                        gestorDePause.pause();
                    } else {
                        gestorDePause.voltar();
                    }
                } else {
                    gerenciadorDeSetas.processarTecla(event.getCode());
                }
            }
        });
    }

    protected void atualizarPontuacao(boolean acerto) {
        if (jogoTerminou) return;

        pontuacao += acerto ? GANHO_POR_ACERTO : -PENALIDADE_POR_ERRO;
        pontuacao = Math.max(0.0, Math.min(1.0, pontuacao));
        placarDeVida.atualizar(pontuacao, null);

        if (pontuacao <= 0) {
            mostrarTelaFinal(false);
        }
    }

    protected void verificarResultadoFinal() {
        boolean venceu = pontuacao > 0.5;
        mostrarTelaFinal(venceu);
    }

    protected void mostrarTelaFinal(boolean vitoria) {
        if (jogoTerminou) return;
        jogoTerminou = true;

        gerenciadorDeSetas.setJogoTerminou(true);
        if (audio != null) audio.stop();
        gerenciadorDeSetas.stopArrowSpawning();

        Stage stage = (Stage) telaFase.getScene().getWindow();
        if (stage != null)
            FinalizarFase.finalizarFase(stage, vitoria);
    }

    protected void showErrorImage() {
        if (erroImageView != null) {
            return;
        }
        try {
            Image erroImage = new Image(getClass().getResourceAsStream("/assets/erro/erro.png"));
            erroImageView = new ImageView(erroImage);

            // Reduz o tamanho da imagem para 100x100 pixels
            erroImageView.setFitWidth(150);
            erroImageView.setFitHeight(150);

            double paneWidth = telaFase.getWidth();
            double paneHeight = telaFase.getHeight();
            double imageWidth = erroImageView.getFitWidth();
            double imageHeight = erroImageView.getFitHeight();

            erroImageView.setX((paneWidth - imageWidth) / 2);
            erroImageView.setY((paneHeight - imageHeight) / 2);

            telaFase.getChildren().add(erroImageView);

            PauseTransition pt = new PauseTransition(Duration.millis(250));
            pt.setOnFinished(e -> {
                telaFase.getChildren().remove(erroImageView);
                erroImageView = null;
            });
            pt.play();
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem de erro: assets/erro/erro.png");
            e.printStackTrace();
        }
    }
}