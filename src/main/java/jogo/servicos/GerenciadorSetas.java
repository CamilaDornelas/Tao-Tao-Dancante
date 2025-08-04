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
import java.util.function.Function; // Nova importação para a action de erro

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

    // Adicione a nova variável para a ação de erro
    private Runnable erroAction;

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

    // Adicione o método para definir a ação de erro
    public void setErroAction(Runnable erroAction) {
        this.erroAction = erroAction;
    }


    public GerenciadorSetas(AnchorPane tela, Rectangle hitZone, MediaPlayer audio, PlacarDeVida placar, Runnable aoFinalDaFase) {
        this.tela = tela;
        this.hitZone = hitZone;
        this.audio = audio;
        this.placar = placar;
        this.aoFinalDaFase = aoFinalDaFase;
    }


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


        int[] setas = { 0, 1, 2, 3, 2, 1, 3, 2, 0, 1, 0, 3, 2, 3, 2, 1, 2, 0, 1, 3, 3, 2, 1, 0,
                2, 2, 2, 3, 0, 3, 1, 0, 2, 1, 0, 1, 3, 0, 0, 1, 3, 2, 3, 0, 1, 2, 0, 1, 3, 3, 2, 2, 1, 0,
                3, 1, 2, 3, 0, 0, 2, 3, 3, 1, 2, 3, 0, 0, 0, 1, 1, 1, 2, 3, 2, 3, 0, 1, 2, 0, 1, 3, 3, 2, 2, 1, 0,
                2, 3, 3, 2, 3, 1, 2, 1, 2, 3, 0, 0, 1, 3, 2, 1, 0, 0, 2, 2, 3, 3, 1, 1, 2, 2, 2, 3, 3, 3, 0, 1, 2, 3, 3};

        Setas.TipoSetas tipo = Setas.TipoSetas.values()[setas[indice++]];
        Setas novaSeta = new Setas(tipo, arrowWidth, arrowHeight, erroAction);

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
                // Em vez de chamar atualizarPontuacao(false) e errar() diretamente,
                // vamos fazer a seta chamar a sua própria lógica de 'errar', que já foi modificada para chamar a ação de erro.
                novaSeta.errar(tela, setasAtivas);
                atualizadorDePontuacao.accept(false);
            }
        });
    }

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

            // Chame a ação de erro aqui
            if (erroAction != null) {
                erroAction.run();
            }
        }
    }


    public List<Setas> getSetasAtivas() {
        return setasAtivas;
    }

    public void mostrarTelaFinal(boolean vitoria) { // Este metodo foi movido ou adaptado do Fase1Controller
        if (jogoTerminou) return;
        jogoTerminou = true;
        if (audio != null) audio.stop();
        stopArrowSpawning(); // Isso ainda depende do gerenciador de setas ter acesso ao timelineSpawn.
        aoFinalDaFase.run();
    }

    public void pauseSpawn() {

    }

    public void resumeSpawn() {

    }


}