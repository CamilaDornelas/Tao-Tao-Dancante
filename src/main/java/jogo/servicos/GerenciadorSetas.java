package jogo.servicos;

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
import java.lang.Runnable;

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

    private Runnable erroAction; // Ação para quando a tecla for apertada fora do tempo
    private Runnable missAction; // Ação para quando a seta passar da zona de acerto

    private boolean jogoTerminou = false;
    private boolean jogoPausado = false;
    
    // ✨ NOVA: Sequência de setas única da fase (vem do JSON)
    private int[] sequenciaSetasFase;

    // ✨ NOVO: Configurações de tempo vindas do JSON
    private LeitorJSONSimples.ConfiguracoesTempo configTempo;


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

    public void setErroAction(Runnable erroAction) {
        this.erroAction = erroAction;
    }

    // Adiciona o metodo para definir a ação de miss
    public void setMissAction(Runnable missAction) {
        this.missAction = missAction;
    }

    // ✨ NOVO: Define a sequência de setas única da fase
    public void setSequenciaSetasFase(int[] sequencia) {
        this.sequenciaSetasFase = sequencia;
        this.indice = 0; // Reseta o índice quando muda a sequência
    }
    
    // ✨ NOVO: Carrega dados diretamente do JSON para uma fase
    public void carregarDadosDaFase(int numeroFase) {
        System.out.println("📖 Carregando dados da fase " + numeroFase + " diretamente do JSON...");
        
        this.sequenciaSetasFase = LeitorJSONSimples.carregarSequenciaSetas(numeroFase);
        this.configTempo = LeitorJSONSimples.carregarConfiguracoesTempo(numeroFase);
        this.indice = 0;
        
        System.out.println("✅ Fase " + numeroFase + " carregada: " + sequenciaSetasFase.length + " setas");
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

        audio.setOnEndOfMedia(aoFinalDaFase);
    }



    public void pararSetas() {
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

    public void setasSubindo() {
        if (jogoTerminou || jogoPausado) return;

        // ✨ NOVA: Usa sequência de setas única da fase (do JSON)
        if (sequenciaSetasFase == null || sequenciaSetasFase.length == 0) {
            System.out.println("⚠️ Nenhuma sequência de setas definida para esta fase!");
            return;
        }

        // Verificação para evitar ArrayIndexOutOfBoundsException
        if (indice >= sequenciaSetasFase.length) {
            System.out.println("🎵 Sequência de setas da fase concluída!");
            return; // Para de criar setas
        }

        // ✨ ATUALIZADO: Usa array da sequência única da fase
        Setas.TipoSetas tipo = Setas.TipoSetas.values()[sequenciaSetasFase[indice++]];
        Setas novaSeta = new Setas(tipo, arrowWidth, arrowHeight, this::setaMissed);

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

        double duracaoAtual = fornecedorDeDuracao != null ? fornecedorDeDuracao.get() : 3000;
        novaSeta.subirSetas(duracaoAtual, subidDistancia).setOnFinished(ev -> {
            if (novaSeta.isVisible()) {
                novaSeta.errar(tela, setasAtivas);
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
                    acertou = true;
                    
                    // ✨ CORRIGIDO: Primeiro brilha, DEPOIS some (com delay pequeno)
                    seta.aplicarEfeitoBrilho();
                    
                    // Delay pequeno para ver o brilho
                    PauseTransition delay = new PauseTransition(Duration.seconds(0.15));
                    delay.setOnFinished(event -> {
                        seta.esconder();
                        tela.getChildren().remove(seta);
                    });
                    delay.play();
                    
                    iterator.remove();
                    break;
                }
            }
        }

        if (!acertou) {
            System.out.println("Erro: nenhuma " + tipo + " válida.");
            atualizadorDePontuacao.accept(false);

            if (erroAction != null) {
                erroAction.run();
            }
        }
    }


    public List<Setas> getSetasAtivas() {
        return setasAtivas;
    }


    public void pauseSpawn() { }
    public void resumeSpawn() { }

    // Novo metodo para a ação de 'miss'
    private void setaMissed() {
        if (missAction != null) {
            missAction.run();
        }
        atualizadorDePontuacao.accept(false);
    }
}