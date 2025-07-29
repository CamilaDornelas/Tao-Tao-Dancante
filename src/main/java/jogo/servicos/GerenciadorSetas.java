package jogo.servicos;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import jogo.componentes.PlacarDeVida;
import jogo.componentes.Setas;

import java.util.function.Consumer;
import java.util.function.Supplier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GerenciadorSetas {

    private final AnchorPane tela;
    private final Rectangle hitZone;
    private final MediaPlayer audio;
    private final PlacarDeVida placar;
    private final Runnable aoFinalDaFase;

    private final List<Setas> setasAtivas = new ArrayList<>();
    private final Random random = new Random();
    private Timeline timelineSpawn;

    //private double pontuacao = 0.5;
    private boolean jogoTerminou = false;
    private boolean jogoPausado = false;

    public boolean isJogoPausado() {
        return jogoPausado;
    }

    public void setJogoPausado(boolean jogoPausado) {
        this.jogoPausado = jogoPausado;
    }

    public void setJogoTerminou(boolean jogoTerminou) {
        this.jogoTerminou = jogoTerminou;
    }

    int indice = 0;
    private final double startX = 300;
    private final double spacing = 165;
    private final double arrowWidth = 190;
    private final double arrowHeight = 180;
    private final double initialArrowY = 900;
    private final double subidDistancia = initialArrowY + arrowHeight;

   // private final double initialSubidaDuracao = 6000;
//    private final double finalSubidaDuracao = 3000;
//    private final double tempoDeAceleracao = 28000;
//    private final double duracaoAposAceleracao = 2500;
//    private final double tempoDeAceleracaoMaxima = 74000;
//
//    private final double GANHO_POR_ACERTO = 0.03;
//    private final double PENALIDADE_POR_ERRO = 0.06;


    private Supplier<Double> fornecedorDeDuracao;
    private Consumer<Boolean> atualizadorDePontuacao;
    private Runnable aoIniciarSetas;

    public void setFornecedorDeDuracao(Supplier<Double> callback) {
        this.fornecedorDeDuracao = callback;
    }

    public void setAtualizadorDePontuacao(Consumer<Boolean> callback) {
        this.atualizadorDePontuacao = callback;
    }

    public void setIniciarSetas(Runnable callback) {
        this.aoIniciarSetas = callback;
    }

    public GerenciadorSetas(AnchorPane tela, Rectangle hitZone, MediaPlayer audio, PlacarDeVida placar, Runnable aoFinalDaFase) {
        this.tela = tela;
        this.hitZone = hitZone;
        this.audio = audio;
        this.placar = placar;
        this.aoFinalDaFase = aoFinalDaFase;
    }

//    private Runnable aoIniciarSetas;
//
//    public void setAoIniciarSetas(Runnable callback) {
//        this.aoIniciarSetas = callback;
//    }

    public void iniciar() {
        PauseTransition inicial = new PauseTransition(Duration.seconds(3));
        inicial.setOnFinished(e -> {
            audio.play();
            if (aoIniciarSetas != null) aoIniciarSetas.run();
        });
        inicial.play();

        //audio.setOnEndOfMedia(this::verificarResultadoFinal);
        audio.setOnEndOfMedia(aoFinalDaFase);
    }


//    private void startArrowSpawning() {
//        stopArrowSpawning();
//
//        timelineSpawn = new Timeline(new KeyFrame(Duration.millis(1200), e -> {
//            double atual = audio.getCurrentTime().toMillis();
//            if (atual >= tempoDeAceleracaoMaxima) {
//                timelineSpawn.setRate(2.5);
//            } else if (atual >= tempoDeAceleracao) {
//                timelineSpawn.setRate(1.9);
//            } else {
//                timelineSpawn.setRate(1.0);
//            }
//
//            spawnRandomArrow();
//        }));
//
//        timelineSpawn.setCycleCount(Timeline.INDEFINITE);
//        timelineSpawn.play();
//    }


    public void stopArrowSpawning() {
        if (timelineSpawn != null) {
            timelineSpawn.stop();
        }

        for (Setas seta : new ArrayList<>(setasAtivas)) {
            if (seta.getRiseAnimation() != null) {
                seta.getRiseAnimation().stop();
            }
            tela.getChildren().remove(seta);
        }
        setasAtivas.clear();
    }

    public void spawnRandomArrow() {
        if (jogoTerminou || jogoPausado) return;


        int[] setas = { 0, 1, 2, 3, 2, 1, 3, 2, 0, 1, 0, 3, 2, 3, 2, 1};
        Setas.TipoSetas tipo = Setas.TipoSetas.values()[setas[indice++]];
        Setas novaSeta = new Setas(tipo, arrowWidth, arrowHeight);

        double posX = switch (tipo) {
            case LEFT -> startX - 10;
            case DOWN -> startX + (2 * spacing) - 63;
            case UP -> startX + spacing - 30;
            case RIGHT -> startX + (3 * spacing) - 90;
        };

        novaSeta.setLayoutX(posX);
        novaSeta.setLayoutY(initialArrowY);
        tela.getChildren().add(novaSeta);
        setasAtivas.add(novaSeta);

        //double duracaoAtual = calcularDuracaoSeta();
        double duracaoAtual = fornecedorDeDuracao != null ? fornecedorDeDuracao.get() : 3000;
        novaSeta.subirSetas(duracaoAtual, subidDistancia).setOnFinished(ev -> {
            if (novaSeta.isVisible()) {
                //atualizarPontuacao(false);
                atualizadorDePontuacao.accept(false);
                novaSeta.errar(tela, setasAtivas);
            }
        });
    }

//    private double calcularDuracaoSeta() {
//        double atual = audio.getCurrentTime().toMillis();
//        if (atual >= tempoDeAceleracao) return duracaoAposAceleracao;
//
//        double progresso = Math.min(1.0, atual / tempoDeAceleracao);
//        double atualDuracao = initialSubidaDuracao gerenciadorDeSetas.setAoIniciarSetas(this::startArrowSpawning);
//- ((initialSubidaDuracao - finalSubidaDuracao) * progresso);
//        return Math.max(finalSubidaDuracao, atualDuracao);
//    }

    public void processarTecla(KeyCode tecla) {
        if (this.jogoTerminou) {
            return;
        }
        Setas.TipoSetas tipo = switch (tecla) {
            case LEFT -> Setas.TipoSetas.LEFT;
            case DOWN -> Setas.TipoSetas.DOWN;
            case UP -> Setas.TipoSetas.UP;
            case RIGHT -> Setas.TipoSetas.RIGHT;
            default -> null;
        };

        if (tipo == null) return;

        boolean acertou = false;
        Iterator<Setas> iterator = setasAtivas.iterator();

        while (iterator.hasNext()) {
            Setas seta = iterator.next();
            if (seta.getType() == tipo && seta.isVisible()) {
                double y = seta.getLayoutY() + seta.getTranslateY();
                double topo = hitZone.getLayoutY();
                double base = topo + hitZone.getHeight();

                if (y + seta.getFitHeight() >= topo && y <= base) {
                    System.out.println("Acerto: " + tipo);
                    atualizadorDePontuacao.accept(true);
                    //atualizadorDePontuacao(true);
                    acertou = true;
                    seta.esconder();
                    tela.getChildren().remove(seta);
                    iterator.remove();
                    break;
                }
            }
        }

        if (!acertou) {
            System.out.println("Erro: nenhuma " + tipo + " válida.");
            //atualizarPontuacao(false);
            atualizadorDePontuacao.accept(false);
        }
    }

//    private void atualizarPontuacao(boolean acerto) {
//        if (jogoTerminou) return;
//
//        pontuacao += acerto ? GANHO_POR_ACERTO : -PENALIDADE_POR_ERRO;
//        pontuacao = Math.max(0.0, Math.min(1.0, pontuacao));
//        placar.atualizar(pontuacao, null); // o Lorde pode ser setado externamente se precisar
//
//        if (pontuacao <= 0) {
//            mostrarTelaFinal(false);
//        }
//    }

//    private void verificarResultadoFinal() {
//        mostrarTelaFinal(pontuacao > 0.5);
//    }

//    private void mostrarTelaFinal(boolean vitoria) {
//        if (jogoTerminou) return;
//        jogoTerminou = true;
//        if (audio != null) audio.stop();
//        stopArrowSpawning();
//        aoFinalDaFase.run();
//    }
//    public double getPontuacao() {
//        return pontuacao;
//    }

    public List<Setas> getSetasAtivas() {
        return setasAtivas;
    }

    public void mostrarTelaFinal(boolean vitoria) { // Este método foi movido ou adaptado do Fase1Controller
        if (jogoTerminou) return;
        jogoTerminou = true;
        if (audio != null) audio.stop();
        stopArrowSpawning(); // Isso ainda depende do gerenciador de setas ter acesso ao timelineSpawn.
        aoFinalDaFase.run();
    }

    public void pauseSpawn() {
        // Se timelineSpawn for um campo de GerenciadorSetas, seria assim:
        // if (timelineSpawn != null) {
        //     timelineSpawn.pause();
        // }
        // Caso contrário, esta lógica precisa ser implementada no Fase1Controller.
    }

    public void resumeSpawn() {
        // Se timelineSpawn for um campo de GerenciadorSetas, seria assim:
        // if (timelineSpawn != null) {
        //     timelineSpawn.play();
        // }
        // Caso contrário, esta lógica precisa ser implementada no Fase1Controller.
    }


}
