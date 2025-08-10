package jogo.servicos;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
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

    // Constantes para as dimensões e posições das setas
    private static final double START_X = 300.0;
    private static final double ESPACAMENTO = 165.0;
    private static final double LARGURA_SETA = 190.0;
    private static final double ALTURA_SETA = 180.0;
    private static final double POSICAO_Y_INICIAL_SETA = 900.0;
    private static final double DISTANCIA_SUBIDA = POSICAO_Y_INICIAL_SETA + ALTURA_SETA;
    private static final Duration DURACAO_ESCONDER_SETA_APOS_BRILHO = Duration.seconds(0.15);
    private static final Duration DELAY_INICIAL_JOGO = Duration.seconds(3);

    private final AnchorPane painelPrincipal;
    private final Rectangle zonaAcerto;
    private final MediaPlayer audio;
    private final PlacarDeVida placar;
    private final Runnable acaoFimDeFase;
    private final List<Setas> setasAtivas = new ArrayList<>();
    private final Random random = new Random();
    private Timeline timelineSpawn;
    private Runnable acaoErro;
    private Runnable acaoMiss;
    private boolean jogoTerminou = false;
    private boolean jogoPausado = false;

    // ✨ NOVO: Sequência de setas única da fase (vem do JSON)
    private int[] sequenciaSetasFase;
    private int indiceSequencia = 0;

    // ✨ NOVO: Configurações de tempo vindas do JSON
    private LeitorJSONSimples.ConfiguracoesTempo configuracoesTempo;

    private Supplier<Double> fornecedorDuracaoAnimacao;
    private Consumer<Boolean> atualizadorPontuacao;
    private Runnable acaoAoIniciarSetas;

    public GerenciadorSetas(AnchorPane painelPrincipal, Rectangle zonaAcerto, MediaPlayer audio, PlacarDeVida placar, Runnable acaoFimDeFase) {
        this.painelPrincipal = painelPrincipal;
        this.zonaAcerto = zonaAcerto;
        this.audio = audio;
        this.placar = placar;
        this.acaoFimDeFase = acaoFimDeFase;
    }

    public void setJogoPausado(boolean jogoPausado) {
        this.jogoPausado = jogoPausado;
    }

    public void setJogoTerminou(boolean jogoTerminou) {
        this.jogoTerminou = jogoTerminou;
    }

    // ✨ NOVO: Define a sequência de setas única da fase
    public void setSequenciaSetasFase(int[] sequencia) {
        this.sequenciaSetasFase = sequencia;
        this.indiceSequencia = 0; // Reseta o índice quando muda a sequência
    }

    // ✨ NOVO: Carrega dados diretamente do JSON para uma fase
    public void carregarDadosDaFase(int numeroFase) {
        System.out.println("📖 Carregando dados da fase " + numeroFase + " diretamente do JSON...");

        this.sequenciaSetasFase = LeitorJSONSimples.carregarSequenciaSetas(numeroFase);
        this.configuracoesTempo = LeitorJSONSimples.carregarConfiguracoesTempo(numeroFase);
        this.indiceSequencia = 0;

        System.out.println("✅ Fase " + numeroFase + " carregada: " + sequenciaSetasFase.length + " setas");
    }

    public void setFornecedorDuracaoAnimacao(Supplier<Double> fornecedor) {
        this.fornecedorDuracaoAnimacao = fornecedor;
    }

    public void setAtualizadorPontuacao(Consumer<Boolean> atualizador) {
        this.atualizadorPontuacao = atualizador;
    }

    public void setAcaoAoIniciarSetas(Runnable acao) {
        this.acaoAoIniciarSetas = acao;
    }

    public void setAcaoErro(Runnable acaoErro) {
        this.acaoErro = acaoErro;
    }

    public void setAcaoMiss(Runnable acaoMiss) {
        this.acaoMiss = acaoMiss;
    }

    public void iniciar() {
        PauseTransition delayInicial = new PauseTransition(DELAY_INICIAL_JOGO);
        delayInicial.setOnFinished(e -> {
            audio.play();
            if (acaoAoIniciarSetas != null) {
                acaoAoIniciarSetas.run();
            }
        });
        delayInicial.play();

        audio.setOnEndOfMedia(acaoFimDeFase);
    }

    public void pararSetas() {
        if (timelineSpawn != null) {
            timelineSpawn.stop();
        }

        for (Setas seta : new ArrayList<>(setasAtivas)) {
            ParallelTransition animacaoSubida = seta.getAnimacaoSubida();
            if (animacaoSubida != null && animacaoSubida.getStatus() == Animation.Status.RUNNING) {
                animacaoSubida.stop();
            }
            painelPrincipal.getChildren().remove(seta);
        }
        setasAtivas.clear();
    }

    public void gerarSeta() {
        if (jogoTerminou || jogoPausado) {
            return;
        }

        if (sequenciaSetasFase == null || indiceSequencia >= sequenciaSetasFase.length) {
            System.out.println("🎵 Sequência de setas da fase concluída ou não definida!");
            return;
        }

        Setas.TipoSetas tipo = Setas.TipoSetas.values()[sequenciaSetasFase[indiceSequencia++]];
        Setas novaSeta = new Setas(tipo, LARGURA_SETA, ALTURA_SETA, this::lidarComSetaPerdida);

        double posX = calcularPosicaoX(tipo);

        novaSeta.setLayoutX(posX);
        novaSeta.setLayoutY(POSICAO_Y_INICIAL_SETA);
        painelPrincipal.getChildren().add(novaSeta);
        setasAtivas.add(novaSeta);

        double duracaoAnimacao = fornecedorDuracaoAnimacao != null ? fornecedorDuracaoAnimacao.get() : 3000;
        novaSeta.iniciarAnimacaoSubida(duracaoAnimacao, DISTANCIA_SUBIDA).setOnFinished(event -> {
            if (novaSeta.isVisible()) {
                novaSeta.lidarComErro(painelPrincipal, setasAtivas);
            }
        });
    }

    private double calcularPosicaoX(Setas.TipoSetas tipo) {
        return switch (tipo) {
            case LEFT -> START_X - 10;
            case DOWN -> START_X + (2 * ESPACAMENTO) - 63;
            case UP -> START_X + ESPACAMENTO - 30;
            case RIGHT -> START_X + (3 * ESPACAMENTO) - 90;
        };
    }

    public void processarTecla(KeyCode tecla) {
        if (this.jogoTerminou) {
            return;
        }

        Setas.TipoSetas tipoTecla = obterTipoSetaPorTecla(tecla);
        if (tipoTecla == null) {
            return;
        }

        boolean acertou = tentarAcertarSeta(tipoTecla);
        if (!acertou) {
            lidarComErroDeAcerto(tipoTecla);
        }
    }

    private Setas.TipoSetas obterTipoSetaPorTecla(KeyCode tecla) {
        return switch (tecla) {
            case LEFT -> Setas.TipoSetas.LEFT;
            case DOWN -> Setas.TipoSetas.DOWN;
            case UP -> Setas.TipoSetas.UP;
            case RIGHT -> Setas.TipoSetas.RIGHT;
            default -> null;
        };
    }

    private boolean tentarAcertarSeta(Setas.TipoSetas tipo) {
        Iterator<Setas> iterator = setasAtivas.iterator();
        while (iterator.hasNext()) {
            Setas seta = iterator.next();
            if (seta.getTipo() == tipo && seta.isVisible()) {
                if (estaNaZonaDeAcerto(seta)) {
                    processarAcerto(seta, iterator);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean estaNaZonaDeAcerto(Setas seta) {
        double y = seta.getLayoutY() + seta.getTranslateY();
        double topoZona = zonaAcerto.getLayoutY();
        double baseZona = topoZona + zonaAcerto.getHeight();
        return y + seta.getFitHeight() >= topoZona && y <= baseZona;
    }

    private void processarAcerto(Setas seta, Iterator<Setas> iterator) {
        System.out.println("Acerto: " + seta.getTipo());
        if (atualizadorPontuacao != null) {
            atualizadorPontuacao.accept(true);
        }

        seta.aplicarEfeitoBrilho();

        PauseTransition delay = new PauseTransition(DURACAO_ESCONDER_SETA_APOS_BRILHO);
        delay.setOnFinished(event -> {
            seta.esconder();
            painelPrincipal.getChildren().remove(seta);
        });
        delay.play();

        iterator.remove();
    }

    private void lidarComErroDeAcerto(Setas.TipoSetas tipo) {
        System.out.println("Erro: nenhuma " + tipo + " válida na zona de acerto.");
        if (atualizadorPontuacao != null) {
            atualizadorPontuacao.accept(false);
        }
        if (acaoErro != null) {
            acaoErro.run();
        }
    }

    public List<Setas> getSetasAtivas() {
        return setasAtivas;
    }

    public void pauseSpawn() {
        if (timelineSpawn != null) {
            timelineSpawn.pause();
        }
    }

    public void resumeSpawn() {
        if (timelineSpawn != null) {
            timelineSpawn.play();
        }
    }

    private void lidarComSetaPerdida() {
        if (acaoMiss != null) {
            acaoMiss.run();
        }
        if (atualizadorPontuacao != null) {
            atualizadorPontuacao.accept(false);
        }
    }
}