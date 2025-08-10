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


public class ControleVolume extends StackPane {

    private static final double LARGURA_TOTAL = 150.0;
    private static final double ALTURA_TOTAL = 20.0;
    private static final double ALTURA_ICONE = 35.0;
    private static final double LARGURA_ICONE = 35.0;
    private static final double INCREMENTO_VOLUME = 0.1;
    private static final double VOLUME_MAXIMO = 1.0;
    private static final double VOLUME_MINIMO = 0.0;

    private static final String COR_FUNDO_BARRA = "#F6FF9E";
    private static final String COR_PREENCHIMENTO_BARRA = "#93481A";

    private Rectangle fundoBarra;
    private Rectangle preenchimentoBarra;
    private ImageView iconeVolume;
    private MediaPlayer reprodutorMidia;
    private double volumeAtual;

    private final GerenciadorPersistenciaVolume persistencia;


    public ControleVolume() {
        this.persistencia = new GerenciadorPersistenciaVolume();

        this.volumeAtual = persistencia.carregarVolume();

        inicializarInterface();
        configurarEventosInterativos();

        System.out.println("Controle de volume criado! Volume atual: " + (int)(volumeAtual * 100) + "%");
    }


    private void inicializarInterface() {
        fundoBarra = criarFundoBarra();
        preenchimentoBarra = criarPreenchimentoBarra();
        iconeVolume = criarIconeVolume();

        HBox layoutPrincipal = new HBox(0);
        layoutPrincipal.setAlignment(Pos.CENTER_LEFT);

        StackPane barraVolume = new StackPane();
        barraVolume.getChildren().addAll(fundoBarra, preenchimentoBarra);

        layoutPrincipal.getChildren().addAll(iconeVolume, barraVolume);

        this.getChildren().add(layoutPrincipal);
    }


    private Rectangle criarFundoBarra() {
        Rectangle fundo = new Rectangle(LARGURA_TOTAL, ALTURA_TOTAL);
        fundo.setFill(Color.web(COR_FUNDO_BARRA));
        fundo.setStroke(Color.DARKGRAY);
        fundo.setStrokeWidth(1);
        return fundo;
    }


    private Rectangle criarPreenchimentoBarra() {
        Rectangle preenchimento = new Rectangle(LARGURA_TOTAL * volumeAtual, ALTURA_TOTAL);
        preenchimento.setFill(Color.web(COR_PREENCHIMENTO_BARRA));
        StackPane.setAlignment(preenchimento, Pos.CENTER_LEFT);
        return preenchimento;
    }


    private ImageView criarIconeVolume() {
        ImageView icone = new ImageView();
        icone.setFitHeight(ALTURA_ICONE);
        icone.setFitWidth(LARGURA_ICONE);
        atualizarImagemIconeVolume();

        icone.setOnMouseClicked(event -> {
            event.consume(); // Impede que o clique se propague para a barra
            alternarMudo();
        });

        return icone;
    }


    private void configurarEventosInterativos() {
        this.setOnMouseClicked(event -> {
            ajustarVolumePelaPosicao(event.getX());
        });

        this.setOnMouseDragged(event -> {
            ajustarVolumePelaPosicao(event.getX());
        });
    }


    private void ajustarVolumePelaPosicao(double posicaoX) {
        double novoVolume = Math.max(VOLUME_MINIMO, Math.min(VOLUME_MAXIMO, posicaoX / LARGURA_TOTAL));
        definirVolume(novoVolume);
    }


    public void setReprodutorMidia(MediaPlayer reprodutorMidia) {
        this.reprodutorMidia = reprodutorMidia;
        if (this.reprodutorMidia != null) {
            this.reprodutorMidia.setVolume(volumeAtual);
        }
    }


    public void definirVolume(double volume) {
        this.volumeAtual = Math.max(VOLUME_MINIMO, Math.min(VOLUME_MAXIMO, volume));

        atualizarVisual();

        if (reprodutorMidia != null) {
            reprodutorMidia.setVolume(this.volumeAtual);
        }
        persistencia.salvarVolume(this.volumeAtual);

        System.out.println("🔊 Volume ajustado para: " + (int)(this.volumeAtual * 100) + "%");
    }


    private void atualizarVisual() {
        preenchimentoBarra.setWidth(LARGURA_TOTAL * volumeAtual);
        atualizarImagemIconeVolume();
    }

    private void atualizarImagemIconeVolume() {
        String novoCaminho = obterCaminhoIcone();
        try {
            Image novaImg = new Image(Objects.requireNonNull(getClass().getResource(novoCaminho)).toExternalForm());
            iconeVolume.setImage(novaImg);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar imagem: " + novoCaminho);
        }
    }


    private String obterCaminhoIcone() {
        if (volumeAtual < 0.01) {
            return "/assets/volume/semVolume.png";
        } else if (volumeAtual <= 0.5) {
            return "/assets/volume/volumeMetade.png";
        } else {
            return "/assets/volume/volume.png";
        }
    }


    public double getVolume() {
        return volumeAtual;
    }


    public void aumentarVolume() {
        definirVolume(volumeAtual + INCREMENTO_VOLUME);
    }


    public void diminuirVolume() {
        definirVolume(volumeAtual - INCREMENTO_VOLUME);
    }


    public void alternarMudo() {
        if (volumeAtual > VOLUME_MINIMO) {
            definirVolume(VOLUME_MINIMO);
        } else {
            definirVolume(VOLUME_MAXIMO);
        }
    }
}