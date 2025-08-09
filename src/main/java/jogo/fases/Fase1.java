package jogo.fases;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import jogo.personagens.BardoDanca;
import jogo.personagens.Lorde;

public class Fase1 extends FaseBase {

    private final double initialSubidaDuracao = 6000;
    private final double finalSubidaDuracao = 3000;
    private final double tempoDeAceleracao = 28000;
    private final double duracaoAposAceleracao = 2500;
    private final double tempoDeAceleracaoMaxima = 74000;

    private Timeline timelineSpawn;

    @Override
    protected void inicializarBackground() {
        definirBackground("/assets/imagens/fase1.png");
    }

    @Override
    protected void inicializarPersonagens() {
        bardo = new BardoDanca(282, 415);
        bardo.setLayoutX(890);
        bardo.setLayoutY(335);
        telaFase.getChildren().add(bardo);

        lorde = new Lorde(210, 380);
        lorde.setLayoutX(120);
        lorde.setLayoutY(370);
        telaFase.getChildren().add(lorde);
    }

    @Override
    protected void inicializarMusica() {
        String musicaPath = getClass().getResource("/assets/musica/song1.mp3").toExternalForm();
        Media media = new Media(musicaPath);
        audio = new MediaPlayer(media);
    }

    @Override
    protected void iniciarFase() {
        pontuacao = 0.5;

        gerenciadorDeSetas.setFornecedorDeDuracao(this::calcularDuracaoSeta);
        gerenciadorDeSetas.setAtualizadorDePontuacao(this::atualizarPontuacao);
        gerenciadorDeSetas.setIniciarSetas(this::startArrowSpawning);

        gerenciadorDeSetas.iniciar();
    }

    private void startArrowSpawning() {
        gerenciadorDeSetas.pararSetas();

        timelineSpawn = new Timeline(new KeyFrame(Duration.millis(1200), e -> {
            double atual = audio.getCurrentTime().toMillis();
            timelineSpawn.setRate(
                    atual >= tempoDeAceleracaoMaxima ? 2.5 :
                            atual >= tempoDeAceleracao ? 1.9 : 1.0
            );
            gerenciadorDeSetas.setasSubindo();
        }));

        timelineSpawn.setCycleCount(Timeline.INDEFINITE);
        timelineSpawn.play();
    }

    private double calcularDuracaoSeta() {
        double atual = audio.getCurrentTime().toMillis();
        if (atual >= tempoDeAceleracao) return duracaoAposAceleracao;
        double progresso = Math.min(1.0, atual / tempoDeAceleracao);
        return initialSubidaDuracao - ((initialSubidaDuracao - finalSubidaDuracao) * progresso);
    }


}


