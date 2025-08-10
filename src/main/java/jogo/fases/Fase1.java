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

    // ✨ SIMPLIFICADO: Dados diretamente do JSON
    private String caminhoMusica;
    private String imagemBackground;
    private LeitorJSONSimples.ConfiguracoesTempo configTempo;
    
    // Configurações antigas mantidas como backup
    private final double initialSubidaDuracao = 6000;
    private final double finalSubidaDuracao = 3000;
    private final double tempoDeAceleracao = 28000;
    private final double duracaoAposAceleracao = 2500;
    private final double tempoDeAceleracaoMaxima = 74000;

    private Timeline timelineSpawn;
    
    public Fase1() {
        // ✨ CARREGA DADOS DIRETAMENTE DO JSON (SEM INTERMEDIÁRIOS)
        System.out.println("📖 Carregando Fase 1 diretamente do JSON...");
        
        this.caminhoMusica = LeitorJSONSimples.carregarCaminhoMusica(1);
        this.configTempo = LeitorJSONSimples.carregarConfiguracoesTempo(1);
        this.imagemBackground = "/assets/imagens/fase1.png"; // Pode vir do JSON também
        
        System.out.println("✅ Fase 1 carregada com sucesso!");
        System.out.println("� Música: " + caminhoMusica);
    }

    @Override
    protected void inicializarBackground() {
        // ✨ USA BACKGROUND ESPECÍFICO DA FASE
        definirBackground(imagemBackground);
        System.out.println("🖼️ Background carregado: " + imagemBackground);
    }

    @Override
    protected void inicializarPersonagens() {
        try {
            // ✨ USA IMAGENS PADRÃO (podem vir do JSON futuramente)
            bardo = new Bardo(282, 415);
            bardo.setLayoutX(890);
            bardo.setLayoutY(335);
            telaFase.getChildren().add(bardo);
            System.out.println("🎭 Bardo carregado");

            lorde = new Lorde(210, 380);
            lorde.setLayoutX(120);
            lorde.setLayoutY(370);
            telaFase.getChildren().add(lorde);
            System.out.println("👑 Lorde carregado");
        } catch (Exception e) {
            System.err.println("Erro ao criar personagens: " + e.getMessage());
        }
    }

    @Override
    protected void inicializarMusica() {
        // ✨ USA MÚSICA DIRETAMENTE DO JSON
        String musicaPath = getClass().getResource(caminhoMusica).toExternalForm();
        Media media = new Media(musicaPath);
        audio = new MediaPlayer(media);
        
        // ✨ NOVO: Aplicar volume salvo imediatamente após criar o MediaPlayer
        jogo.servicos.GerenciadorPersistenciaVolume persistencia = new jogo.servicos.GerenciadorPersistenciaVolume();
        double volumeSalvo = persistencia.carregarVolume();
        audio.setVolume(volumeSalvo);
        
        System.out.println("🎵 Música carregada: " + caminhoMusica);
        System.out.println("🔊 Volume aplicado: " + (int)(volumeSalvo * 100) + "%");
    }

    @Override
    protected void iniciarFase() {
        pontuacao = 0.5;

        // ✨ CARREGA DADOS DIRETAMENTE NO GERENCIADOR
        gerenciadorDeSetas.carregarDadosDaFase(1);

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
        
        // ✨ USA CONFIGURAÇÕES DO JSON
        if (configTempo != null) {
            return configTempo.calcularDuracao(atual);
        }
        
        // Fallback para lógica antiga
        if (atual >= tempoDeAceleracao) return duracaoAposAceleracao;
        double progresso = Math.min(1.0, atual / tempoDeAceleracao);
        return initialSubidaDuracao - ((initialSubidaDuracao - finalSubidaDuracao) * progresso);
    }
    
    /**
     * ✨ SIMPLIFICADO: Informações básicas da fase
     */
    public String obterInformacoesFase() {
        return "Fase 1: Despertar do Bardo [FÁCIL]";
    }


}


