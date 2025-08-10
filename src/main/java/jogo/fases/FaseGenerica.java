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

    private final int numeroFase;
    private DadosFase dadosFase;
    private Timeline timelineSpawn;
    
    /**
     * Construtor que recebe o número da fase
     */
    public FaseGenerica(int numeroFase) {
        this.numeroFase = numeroFase;
        
        // ✨ CARREGA DADOS COMPLETOS DO JSON
        System.out.println("📖 Carregando Fase " + numeroFase + " do JSON...");
        
        try {
            // Carrega dados completos da fase do JSON
            this.dadosFase = LeitorDadosJSON.carregarFaseDoJSON(numeroFase);
            System.out.println("✅ " + dadosFase.toString());
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar fase " + numeroFase + ": " + e.getMessage());
            // Fallback para dados básicos se JSON falhar
            carregarDadosBasicos();
        }
    }
    
    /**
     * Fallback para carregar dados básicos se JSON não funcionar
     */
    private void carregarDadosBasicos() {
        System.out.println("⚠️ Usando dados básicos para fase " + numeroFase);
        
        // Dados básicos baseados no número da fase
        String caminhoMusica = String.format("/assets/musica/song%d.mp3", numeroFase);
        String imagemBackground = String.format("/assets/imagens/fase%d.png", numeroFase);
        
        // Cria dados mínimos usando o método criarDoJSON
        int[] setasBasicas = {0, 1, 2, 3, 2, 1, 3, 2, 0, 1}; // Sequência básica
        
        this.dadosFase = DadosFase.criarDoJSON(
            numeroFase,
            "Fase " + numeroFase,
            "NORMAL",
            1.0 + (numeroFase - 1) * 0.5, // Peso aumenta com a fase
            setasBasicas,
            caminhoMusica,
            "/assets/persona/bardoDance1.png",
            "/assets/persona/lordBarra.png",
            imagemBackground,
            6000.0 - (numeroFase - 1) * 1000, // Fica mais rápido
            3000.0 - (numeroFase - 1) * 500,  // Fica mais rápido
            30000.0 - (numeroFase - 1) * 5000 // Acelera mais cedo
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
            bardo = new Bardo(282, 415);
            bardo.setLayoutX(890);
            bardo.setLayoutY(335);
            telaFase.getChildren().add(bardo);
            System.out.println("🎭 Bardo carregado: " + dadosFase.getImagemBardo());

            lorde = new Lorde(210, 380);
            lorde.setLayoutX(120);
            lorde.setLayoutY(370);
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
        audio = new MediaPlayer(media);
        
        // ✨ APLICA VOLUME SALVO IMEDIATAMENTE
        jogo.servicos.GerenciadorPersistenciaVolume persistencia = new jogo.servicos.GerenciadorPersistenciaVolume();
        double volumeSalvo = persistencia.carregarVolume();
        audio.setVolume(volumeSalvo);
        
        System.out.println("🎵 Música carregada: " + dadosFase.getCaminhoMusica());
        System.out.println("🔊 Volume aplicado: " + (int)(volumeSalvo * 100) + "%");
    }

    @Override
    protected void iniciarFase() {
        // ✨ USA PESO DE PONTUAÇÃO DA FASE
        pontuacao = dadosFase.getPesoPontuacao() * 0.5; // Ajuste inicial baseado no peso

        // ✨ CARREGA DADOS DIRETAMENTE NO GERENCIADOR
        gerenciadorDeSetas.carregarDadosDaFase(numeroFase);
        
        // ✨ CONFIGURA TODOS OS CALLBACKS NECESSÁRIOS
        gerenciadorDeSetas.setFornecedorDeDuracao(this::calcularDuracaoSeta);
        gerenciadorDeSetas.setAtualizadorDePontuacao(this::atualizarPontuacao);
        gerenciadorDeSetas.setIniciarSetas(this::startArrowSpawning);
        
        // ✨ CRÍTICO: Inicia o gerenciador (música + spawn de setas)
        gerenciadorDeSetas.iniciar();
        
        System.out.println("🎯 Fase " + numeroFase + " iniciada!");
        System.out.println("📊 Peso da pontuação: " + dadosFase.getPesoPontuacao() + "x");
    }
    
    /**
     * ✨ CALCULA DURAÇÃO DAS SETAS BASEADO NA FASE
     */
    private Double calcularDuracaoSeta() {
        if (audio == null) return dadosFase.getDuracaoSetasInicial();
        
        double tempoAtual = audio.getCurrentTime().toMillis();
        double tempoAceleracao = dadosFase.getTempoAceleracao();
        
        // Acelera conforme o tempo da música
        if (tempoAtual >= tempoAceleracao) {
            return dadosFase.getDuracaoSetasFinal(); // Mais rápido
        } else {
            return dadosFase.getDuracaoSetasInicial(); // Velocidade normal
        }
    }
    
    /**
     * ✨ INICIA O SPAWN DE SETAS
     */
    private void startArrowSpawning() {
        gerenciadorDeSetas.pararSetas();

        timelineSpawn = new Timeline(new KeyFrame(Duration.millis(1200), e -> {
            double atual = audio.getCurrentTime().toMillis();
            double tempoAceleracao = dadosFase.getTempoAceleracao();
            
            // Ajusta velocidade baseado no tempo da música
            timelineSpawn.setRate(
                atual >= tempoAceleracao * 1.5 ? 2.5 :  // Super rápido
                atual >= tempoAceleracao ? 1.9 :         // Rápido
                1.0                                      // Normal
            );
            
            gerenciadorDeSetas.setasSubindo();
        }));

        timelineSpawn.setCycleCount(Timeline.INDEFINITE);
        timelineSpawn.play();
    }
    
    /**
     * Obtém o número da fase
     */
    public int getNumeroFase() {
        return numeroFase;
    }
}
