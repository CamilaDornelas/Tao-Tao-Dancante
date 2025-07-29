package jogo.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import jogo.componentes.PlacarDeVida;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import jogo.interfaces.Pause;
import jogo.personagens.BardoDanca;
import jogo.personagens.Lorde;
import jogo.interfaces.GestorDePause;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import jogo.servicos.FinalizarFase;
import jogo.servicos.GerenciadorSetas;





public class Fase1Controller {

    private Pause GestorDePause;
    private GerenciadorSetas gerenciadorDeSetas;

    @FXML
    private AnchorPane telaFase1;

    private Rectangle hitZone;
    private MediaPlayer audio;
    private PlacarDeVida placarDeVida;

    private Lorde lorde;
    private BardoDanca bardo;

    private boolean jogoTerminou = false;
    @FXML
    private void initialize() {
        inicializarPersonagens();
        inicializarHitZone();
        inicializarPlacar();
        inicializarMusica();
        inicializarGerenciadorDeSetas();
        configurarTeclado();
        iniciarFase();
    }

    private final double initialSubidaDuracao = 6000;
    private final double finalSubidaDuracao = 3000;
    private final double tempoDeAceleracao = 28000;
    private final double duracaoAposAceleracao = 2500;
    private final double tempoDeAceleracaoMaxima = 74000;
    private double pontuacao = 0.5;

    private final double GANHO_POR_ACERTO = 0.03;
    private final double PENALIDADE_POR_ERRO = 0.06;


    public void iniciarFase() {
        //gerenciadorDeSetas = new GerenciadorSetas(telaFase1, hitZone, audio, placarDeVida, this::mostrarTelaFinal);
        //gerenciadorDeSetas = new GerenciadorSetas(telaFase1, hitZone, audio, placarDeVida, this::mostrarTelaFinal);
//        gerenciadorDeSetas.setAoIniciarSetas(this::startArrowSpawning); // ⬅ Aqui é o ponto!
        gerenciadorDeSetas.setFornecedorDeDuracao(this::calcularDuracaoSeta);
        gerenciadorDeSetas.setAtualizadorDePontuacao(this::atualizarPontuacao);
        gerenciadorDeSetas.setIniciarSetas(this::startArrowSpawning);



        gerenciadorDeSetas.iniciar();


    }


    private void inicializarPersonagens() {
        bardo = new BardoDanca(282, 415);
        bardo.setLayoutX(890);
        bardo.setLayoutY(335);
        telaFase1.getChildren().add(bardo);

        lorde = new Lorde(210, 380);
        lorde.setLayoutX(120);
        lorde.setLayoutY(370);
        telaFase1.getChildren().add(lorde);
    }

    private void inicializarHitZone() {
        hitZone = new Rectangle(300, 15, (3 * 165) + 190, 130);
        hitZone.setFill(Color.TRANSPARENT);
        hitZone.setStroke(Color.TRANSPARENT);
        hitZone.setStrokeWidth(3);
        telaFase1.getChildren().add(hitZone);
    }

    private void inicializarPlacar() {
        placarDeVida = new PlacarDeVida();
        placarDeVida.setLayoutX(400);
        placarDeVida.setLayoutY(700);
        telaFase1.getChildren().add(placarDeVida);
    }

    private void inicializarMusica() {
        String musicaPath = getClass().getResource("/assets/musica/song1.mp3").toExternalForm();
        Media midia = new Media(musicaPath);
        audio = new MediaPlayer(midia);
    }

    private void inicializarGerenciadorDeSetas() {
        gerenciadorDeSetas = new GerenciadorSetas(telaFase1, hitZone, audio, placarDeVida, () -> {
            verificarResultadoFinal();
        });
        //gerenciadorDeSetas.iniciar();
        System.out.println(gerenciadorDeSetas.getSetasAtivas());
        GestorDePause = new GestorDePause(
                telaFase1,
                bardo.getAnimacao(),
                audio,
                gerenciadorDeSetas.getSetasAtivas(),
                null,
                gerenciadorDeSetas
        );

        //GestorDePause = new GestorDePause(telaFase1, bardo.getAnimacao(), audio, null, gerenciadorDeSetas::iniciar);
    }

    private void configurarTeclado() {
        telaFase1.setFocusTraversable(true);
        Platform.runLater(() -> telaFase1.requestFocus());

        telaFase1.setOnKeyPressed(event -> {
            if (!jogoTerminou) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    if (!GestorDePause.estaPausado()) {
                        GestorDePause.pause();
                      //  gerenciadorDeSetas.stopArrowSpawning();
                    } else {
                        GestorDePause.voltar();
                       // gerenciadorDeSetas.iniciar();
                    }
                } else {
                    gerenciadorDeSetas.processarTecla(event.getCode());
                }
            }
        });


    }
    private void mostrarTelaFinal(boolean vitoria) {
        if (jogoTerminou) return;
        jogoTerminou = true;

        gerenciadorDeSetas.setJogoTerminou(true);

        if (audio != null) audio.stop();
        gerenciadorDeSetas.stopArrowSpawning();

        Stage stage = (Stage) telaFase1.getScene().getWindow();
        if (stage == null) {
            System.err.println("Erro: O Stage é nulo ao tentar mostrar a tela final. A janela pode ter sido fechada ou não está disponível.");
            return;
        }
        FinalizarFase.finalizarFase(stage, vitoria);
    }
    private void verificarResultadoFinal() {
        double pontuacao = gerenciadorDeSetas.getPontuacao();
        boolean venceu = pontuacao > 0.5;
        mostrarTelaFinal(venceu);
    }

    private Timeline timelineSpawn;

    private void startArrowSpawning() {
        gerenciadorDeSetas.stopArrowSpawning();


        timelineSpawn = new Timeline(new KeyFrame(Duration.millis(1200), e -> {
            double atual = audio.getCurrentTime().toMillis();
            if (atual >= tempoDeAceleracaoMaxima) {
                timelineSpawn.setRate(2.5);
            } else if (atual >= tempoDeAceleracao) {
                timelineSpawn.setRate(1.9);
            } else {
                timelineSpawn.setRate(1.0);
            }

            gerenciadorDeSetas.spawnRandomArrow();
        }));

        timelineSpawn.setCycleCount(Timeline.INDEFINITE);
        timelineSpawn.play();
    }

    private double calcularDuracaoSeta() {
        double atual = audio.getCurrentTime().toMillis();
        if (atual >= tempoDeAceleracao) return duracaoAposAceleracao;

        double progresso = Math.min(1.0, atual / tempoDeAceleracao);
        double atualDuracao = initialSubidaDuracao - ((initialSubidaDuracao - finalSubidaDuracao) * progresso);
        return Math.max(finalSubidaDuracao, atualDuracao);
    }

    private void atualizarPontuacao(boolean acerto) {
        if (jogoTerminou) return;

        pontuacao += acerto ? GANHO_POR_ACERTO : -PENALIDADE_POR_ERRO;
        pontuacao = Math.max(0.0, Math.min(1.0, pontuacao));
        placarDeVida.atualizar(pontuacao, null);

        if (pontuacao <= 0) {
            mostrarTelaFinal(false);
        }
    }


}
