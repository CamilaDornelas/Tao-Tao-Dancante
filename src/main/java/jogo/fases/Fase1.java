package jogo.fases;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import jogo.personagens.Bardo;
import jogo.personagens.Lorde;
import jogo.servicos.LeitorJSONSimples;

public class Fase1 extends FaseBase {

    // Constantes para as posições dos personagens (mantido para consistência)
    private static final double BARDO_POSICAO_X = 890.0;
    private static final double BARDO_POSICAO_Y = 335.0;
    private static final double LORDE_POSICAO_X = 120.0;
    private static final double LORDE_POSICAO_Y = 370.0;
    private static final double BARDO_LARGURA = 282.0;
    private static final double BARDO_ALTURA = 415.0;
    private static final double LORDE_LARGURA = 210.0;
    private static final double LORDE_ALTURA = 380.0;

    // Constantes para a lógica de spawn (backup)
    private static final double DURACAO_SPAWN_SETA_MILLIS = 1200.0;
    private static final double DURACAO_SUBIDA_INICIAL_BACKUP = 6000.0;
    private static final double DURACAO_SUBIDA_FINAL_BACKUP = 3000.0;
    private static final double TEMPO_ACELERACAO_BACKUP = 28000.0;
    private static final double DURACAO_APOS_ACELERACAO_BACKUP = 2500.0;
    private static final double TEMPO_ACELERACAO_MAXIMA_BACKUP = 74000.0;

    // ✨ SIMPLIFICADO: Dados diretamente do JSON
    private String caminhoMusica;
    private String imagemBackground;
    private LeitorJSONSimples.ConfiguracoesTempo configuracoesTempo;

    private Timeline timelineSpawn;

    public Fase1() {
        System.out.println("📖 Carregando Fase 1 diretamente do JSON...");

        this.caminhoMusica = LeitorJSONSimples.carregarCaminhoMusica(1);
        this.configuracoesTempo = LeitorJSONSimples.carregarConfiguracoesTempo(1);
        this.imagemBackground = "/assets/imagens/fase1.png";

        System.out.println("✅ Fase 1 carregada com sucesso!");
        System.out.println("🎵 Música: " + caminhoMusica);
    }

    @Override
    protected void inicializarBackground() {
        definirBackground(imagemBackground);
        System.out.println("🖼️ Background carregado: " + imagemBackground);
    }

    @Override
    protected void inicializarPersonagens() {
        try {
            bardo = new Bardo(BARDO_LARGURA, BARDO_ALTURA);
            bardo.setLayoutX(BARDO_POSICAO_X);
            bardo.setLayoutY(BARDO_POSICAO_Y);
            telaFase.getChildren().add(bardo);
            System.out.println("🎭 Bardo carregado");

            lorde = new Lorde(LORDE_LARGURA, LORDE_ALTURA);
            lorde.setLayoutX(LORDE_POSICAO_X);
            lorde.setLayoutY(LORDE_POSICAO_Y);
            telaFase.getChildren().add(lorde);
            System.out.println("👑 Lorde carregado");
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar personagens: " + e.getMessage());
        }
    }

    @Override
    protected void inicializarMusica() {
        String musicaPath = getClass().getResource(caminhoMusica).toExternalForm();
        Media media = new Media(musicaPath);
        reprodutorMidia = new MediaPlayer(media);

        jogo.servicos.GerenciadorPersistenciaVolume persistencia = new jogo.servicos.GerenciadorPersistenciaVolume();
        double volumeSalvo = persistencia.carregarVolume();
        reprodutorMidia.setVolume(volumeSalvo);

        System.out.println("🎵 Música carregada: " + caminhoMusica);
        System.out.println("🔊 Volume aplicado: " + (int)(volumeSalvo * 100) + "%");
    }

    @Override
    protected void iniciarFase() {
        pontuacao = 0.5;

        gerenciadorSetas.carregarDadosDaFase(1);
        gerenciadorSetas.setFornecedorDuracaoAnimacao(this::calcularDuracaoSeta);
        gerenciadorSetas.setAtualizadorPontuacao(this::atualizarPontuacao);
        gerenciadorSetas.setAcaoAoIniciarSetas(this::iniciarSpawnSetas);

        gerenciadorSetas.iniciar();
    }

    private void iniciarSpawnSetas() {
        gerenciadorSetas.pararSetas();

        timelineSpawn = new Timeline(new KeyFrame(Duration.millis(DURACAO_SPAWN_SETA_MILLIS), e -> {
            if (reprodutorMidia != null) {
                ajustarVelocidadeSpawn();
            }
            gerenciadorSetas.gerarSeta();
        }));

        timelineSpawn.setCycleCount(Timeline.INDEFINITE);
        timelineSpawn.play();
    }

    private void ajustarVelocidadeSpawn() {
        double tempoAtual = reprodutorMidia.getCurrentTime().toMillis();
        timelineSpawn.setRate(
                tempoAtual >= TEMPO_ACELERACAO_MAXIMA_BACKUP ? 2.5 :
                        tempoAtual >= TEMPO_ACELERACAO_BACKUP ? 1.9 : 1.0
        );
    }


    private double calcularDuracaoSeta() {
        if (reprodutorMidia == null) {
            return DURACAO_SUBIDA_INICIAL_BACKUP;
        }

        double tempoAtual = reprodutorMidia.getCurrentTime().toMillis();

        if (configuracoesTempo != null) {
            return configuracoesTempo.calcularDuracao(tempoAtual);
        }

        if (tempoAtual >= TEMPO_ACELERACAO_BACKUP) {
            return DURACAO_APOS_ACELERACAO_BACKUP;
        }
        double progresso = Math.min(1.0, tempoAtual / TEMPO_ACELERACAO_BACKUP);
        return DURACAO_SUBIDA_INICIAL_BACKUP - ((DURACAO_SUBIDA_INICIAL_BACKUP - DURACAO_SUBIDA_FINAL_BACKUP) * progresso);
    }

    public String obterInformacoesFase() {
        return "Fase 1: Despertar do Bardo [FÁCIL]";
    }
}