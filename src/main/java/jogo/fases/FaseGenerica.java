package jogo.fases;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import jogo.personagens.Bardo;
import jogo.personagens.Lorde;
import jogo.servicos.LeitorDadosJSON;
import jogo.modelo.DadosFase;

/**
 * ✨ FASE GENÉRICA
 * Pode carregar qualquer fase baseada no número fornecido
 * Substitui a necessidade de criar Fase1, Fase2, Fase3, etc.
 */
public class FaseGenerica extends FaseBase {

    // Constantes para as posições dos personagens
    private static final double BARDO_POSICAO_X = 890.0;
    private static final double BARDO_POSICAO_Y = 335.0;
    private static final double BARDO_LARGURA = 282.0;
    private static final double BARDO_ALTURA = 415.0;

    private static final double LORDE_POSICAO_X = 120.0;
    private static final double LORDE_POSICAO_Y = 370.0;
    private static final double LORDE_LARGURA = 210.0;
    private static final double LORDE_ALTURA = 380.0;

    // Constante para a duração do spawn de setas
    private static final double DURACAO_SPAWN_SETA_MILLIS = 1200.0;

    private final int numeroFase;
    private DadosFase dadosFase;
    private Timeline timelineSpawn;

    /**
     * Construtor que recebe o número da fase
     */
    public FaseGenerica(int numeroFase) {
        this.numeroFase = numeroFase;

        System.out.println("📖 Carregando Fase " + numeroFase + " do JSON...");

        try {
            this.dadosFase = LeitorDadosJSON.carregarFaseDoJSON(numeroFase);
            System.out.println("✅ " + dadosFase.toString());
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar fase " + numeroFase + ": " + e.getMessage());
            carregarDadosDeFallback();
        }
    }

    /**
     * Fallback para carregar dados básicos se JSON não funcionar
     */
    private void carregarDadosDeFallback() {
        System.out.println("⚠️ Usando dados básicos para fase " + numeroFase);

        String caminhoMusica = String.format("/assets/musica/song%d.mp3", numeroFase);
        String caminhoBackground = String.format("/assets/imagens/fase%d.png", numeroFase);

        int[] setasBasicas = {0, 1, 2, 3, 2, 1, 3, 2, 0, 1};

        this.dadosFase = DadosFase.criarDoJSON(
                numeroFase,
                "Fase " + numeroFase,
                "NORMAL",
                1.0 + (numeroFase - 1) * 0.5,
                setasBasicas,
                caminhoMusica,
                "/assets/persona/bardoDance1.png",
                "/assets/persona/lordBarra.png",
                caminhoBackground,
                6000.0 - (numeroFase - 1) * 1000,
                3000.0 - (numeroFase - 1) * 500,
                30000.0 - (numeroFase - 1) * 5000
        );
    }

    @Override
    protected void inicializarBackground() {
        // ✨ USA BACKGROUND DA FASE CARREGADA
        definirBackground(dadosFase.getImagemBackground());
        System.out.println("🖼️ Background carregado: " + dadosFase.getImagemBackground());
    }

    @Override
    protected void inicializarPersonagens() {
        try {
            // ✨ USA IMAGENS DOS PERSONAGENS DA FASE CARREGADA
            bardo = new Bardo(BARDO_LARGURA, BARDO_ALTURA);
            bardo.setLayoutX(BARDO_POSICAO_X);
            bardo.setLayoutY(BARDO_POSICAO_Y);
            telaFase.getChildren().add(bardo);
            System.out.println("🎭 Bardo carregado: " + dadosFase.getImagemBardo());

            lorde = new Lorde(LORDE_LARGURA, LORDE_ALTURA);
            lorde.setLayoutX(LORDE_POSICAO_X);
            lorde.setLayoutY(LORDE_POSICAO_Y);
            telaFase.getChildren().add(lorde);
            System.out.println("👑 Lorde carregado: " + dadosFase.getImagemLorde());
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar personagens: " + e.getMessage());
        }
    }

    @Override
    protected void inicializarMusica() {
        // ✨ USA MÚSICA DA FASE CARREGADA
        String musicaPath = getClass().getResource(dadosFase.getCaminhoMusica()).toExternalForm();
        Media media = new Media(musicaPath);
        reprodutorMidia = new MediaPlayer(media);

        // ✨ APLICA VOLUME SALVO IMEDIATAMENTE
        jogo.servicos.GerenciadorPersistenciaVolume persistencia = new jogo.servicos.GerenciadorPersistenciaVolume();
        double volumeSalvo = persistencia.carregarVolume();
        reprodutorMidia.setVolume(volumeSalvo);

        System.out.println("🎵 Música carregada: " + dadosFase.getCaminhoMusica());
        System.out.println("🔊 Volume aplicado: " + (int)(volumeSalvo * 100) + "%");
    }

    @Override
    protected void iniciarFase() {
        pontuacao = dadosFase.getPesoPontuacao() * 0.5;

        gerenciadorSetas.carregarDadosDaFase(numeroFase);

        gerenciadorSetas.setFornecedorDuracaoAnimacao(this::calcularDuracaoSeta);
        gerenciadorSetas.setAtualizadorPontuacao(this::atualizarPontuacao);
        gerenciadorSetas.setAcaoAoIniciarSetas(this::iniciarSpawnSetas);

        gerenciadorSetas.iniciar();

        System.out.println("🎯 Fase " + numeroFase + " iniciada!");
        System.out.println("📊 Peso da pontuação: " + dadosFase.getPesoPontuacao() + "x");
    }

    /**
     * ✨ CALCULA DURAÇÃO DAS SETAS BASEADO NA FASE
     */
    private Double calcularDuracaoSeta() {
        if (reprodutorMidia == null) return dadosFase.getDuracaoSetasInicial();

        double tempoAtual = reprodutorMidia.getCurrentTime().toMillis();
        double tempoAceleracao = dadosFase.getTempoAceleracao();

        return tempoAtual >= tempoAceleracao ? dadosFase.getDuracaoSetasFinal() : dadosFase.getDuracaoSetasInicial();
    }

    /**
     * ✨ INICIA O SPAWN DE SETAS
     */
    private void iniciarSpawnSetas() {
        gerenciadorSetas.pararSetas();

        timelineSpawn = new Timeline(new KeyFrame(Duration.millis(DURACAO_SPAWN_SETA_MILLIS), e -> {
            ajustarVelocidadeSpawn();
            gerenciadorSetas.gerarSeta();
        }));

        timelineSpawn.setCycleCount(Timeline.INDEFINITE);
        timelineSpawn.play();
    }

    /**
     * Ajusta a velocidade de spawn de setas com base no tempo da música.
     */
    private void ajustarVelocidadeSpawn() {
        if (reprodutorMidia == null) return;

        double tempoAtual = reprodutorMidia.getCurrentTime().toMillis();
        double tempoAceleracao = dadosFase.getTempoAceleracao();

        if (tempoAtual >= tempoAceleracao * 1.5) {
            timelineSpawn.setRate(2.5);
        } else if (tempoAtual >= tempoAceleracao) {
            timelineSpawn.setRate(1.9);
        } else {
            timelineSpawn.setRate(1.0);
        }
    }

    /**
     * Obtém o número da fase
     * @return O número da fase.
     */
    public int getNumeroFase() {
        return numeroFase;
    }
}