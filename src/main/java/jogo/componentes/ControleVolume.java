package jogo.componentes;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jogo.servicos.GerenciadorPersistenciaVolume;
import java.util.Objects;

/**
 * Componente de controle de volume com barra interativa
 * Salva configurações usando o sistema de persistência
 */
public class ControleVolume extends StackPane {

    private final double LARGURA_TOTAL = 150;
    private final double ALTURA_TOTAL = 20;

    private Rectangle fundo;
    private Rectangle preenchimento;
    private ImageView iconeVolume;
    private MediaPlayer mediaPlayer;
    private double volumeAtual = 1.0;
    
    private final GerenciadorPersistenciaVolume persistencia;

    public ControleVolume() {
        this.persistencia = new GerenciadorPersistenciaVolume();
        
        this.volumeAtual = persistencia.carregarVolume();
        
        inicializarComponentes();
        configurarEventos();
        
        System.out.println("🎵 Controle de volume criado! Volume atual: " + (int)(volumeAtual * 100) + "%");
    }

    private void inicializarComponentes() {
        fundo = new Rectangle(LARGURA_TOTAL, ALTURA_TOTAL);
        fundo.setFill(Color.web("#F6FF9E"));
        fundo.setStroke(Color.DARKGRAY);
        fundo.setStrokeWidth(1);

  
        preenchimento = new Rectangle(LARGURA_TOTAL * volumeAtual, ALTURA_TOTAL);
        preenchimento.setFill(Color.web("#93481A"));
        StackPane.setAlignment(preenchimento, Pos.CENTER_LEFT);

        iconeVolume = criarIconeVolume();
        
        HBox layout = new HBox(0);
        layout.setAlignment(Pos.CENTER_LEFT);
        
        StackPane barraVolume = new StackPane();
        barraVolume.getChildren().addAll(fundo, preenchimento);
        
        layout.getChildren().addAll(iconeVolume, barraVolume);
        this.getChildren().add(layout);
    }

    private ImageView criarIconeVolume() {
        String caminhoIcone = obterCaminhoIcone(); 
        
        try {
            Image img = new Image(Objects.requireNonNull(getClass().getResource(caminhoIcone)).toExternalForm());
            ImageView icone = new ImageView(img);
            icone.setFitHeight(35);
            icone.setFitWidth(35);
            
        
            icone.setOnMouseClicked(event -> {
                event.consume(); 
                toggleVolumeComImagem();
            });
            
            return icone;
        } catch (Exception e) {

            ImageView icone = new ImageView();
            icone.setFitHeight(35);
            icone.setFitWidth(35);
            icone.setOnMouseClicked(event -> {
                event.consume();
                toggleVolumeComImagem();
            });
            return icone;
        }
    }

    /**
     * Escolhe a imagem baseada no volume atual
     */
    private String obterCaminhoIcone() {
        if (volumeAtual < 0.01) { 
            return "/assets/volume/semVolume.png";
        } else if (volumeAtual <= 0.5) {
            return "/assets/volume/volumeMetade.png";
        } else {
            return "/assets/volume/volume.png";
        }
    }

    /**
     * Alterna volume ao clicar na imagem
     * volume/volumeMetade → sem volume (0%)
     * semVolume → volume máximo (100%)
     */
    private void toggleVolumeComImagem() {
        if (volumeAtual < 0.01) { 
            setVolume(1.0); // 100%
            System.out.println("🔊 Volume ativado: 100%");
        } else {
            setVolume(0.0); // 0%
            System.out.println("🔇 Volume desativado: 0%");
        }
    }

    private void configurarEventos() {

        this.setOnMouseClicked(event -> {
            double posicaoX = event.getX();
            double novoVolume = Math.max(0, Math.min(1, posicaoX / LARGURA_TOTAL));
            setVolume(novoVolume);
        });
        

        this.setOnMouseDragged(event -> {
            double posicaoX = event.getX();
            double novoVolume = Math.max(0, Math.min(1, posicaoX / LARGURA_TOTAL));
            setVolume(novoVolume);
        });
    }

    /**
     * Define o MediaPlayer para controlar o volume
     */
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volumeAtual);
        }
    }

    /**
     * Ajusta o volume (0.0 a 1.0)
     */
    public void setVolume(double volume) {
        volume = Math.max(0, Math.min(1, volume));
        this.volumeAtual = volume;
        
   
        atualizarVisual();
   
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
        
   
        persistencia.salvarVolume(volume);
        
        System.out.println("🔊 Volume ajustado para: " + (int)(volume * 100) + "%");
    }

    private void atualizarVisual() {
     
        preenchimento.setWidth(LARGURA_TOTAL * volumeAtual);

        preenchimento.setFill(Color.web("#93481A"));
        atualizarIconeVolume();
    }

    /**
     *  Atualiza a imagem do ícone baseada no volume atual
     */
    private void atualizarIconeVolume() {
        String novoCaminho = obterCaminhoIcone();
        
        try {
            Image novaImg = new Image(Objects.requireNonNull(getClass().getResource(novoCaminho)).toExternalForm());
            iconeVolume.setImage(novaImg);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar imagem: " + novoCaminho);
        }
    }

    /**
     * Obtém o volume atual (0.0 a 1.0)
     */
    public double getVolume() {
        return volumeAtual;
    }

    /**
     * Aumenta o volume em 10%
     */
    public void aumentarVolume() {
        setVolume(volumeAtual + 0.1);
    }

    /**
     * Diminui o volume em 10%
     */
    public void diminuirVolume() {
        setVolume(volumeAtual - 0.1);
    }

    /**
     * Silencia/reativa o som
     */
    public void toggleMute() {
        if (volumeAtual > 0) {
            setVolume(0);
        } else {
            setVolume(1.0);
        }
    }
}
