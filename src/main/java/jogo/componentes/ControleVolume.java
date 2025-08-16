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
    private static final double VOLUME_MAXIMO = 1.0;
    private static final double VOLUME_MINIMO = 0.0;

    private static final String COR_FUNDO_BARRA = "#F6FF9E";
    private static final String COR_PREENCHIMENTO_BARRA = "#93481A";

    private Rectangle barraFundo;
    private Rectangle barraPreenchimento;
    private ImageView iconeVolume;
    private MediaPlayer reprodutorMidia;
    private double volumeAtual;

    private final GerenciadorPersistenciaVolume gerenciadorPersistencia;

    public ControleVolume() {
        this.gerenciadorPersistencia = new GerenciadorPersistenciaVolume();
        this.volumeAtual = gerenciadorPersistencia.carregarVolume();

        inicializarInterface();
        configurarEventosInterativos();
    }

    private void inicializarInterface() {
        barraFundo = criarBarraFundo();
        barraPreenchimento = criarBarraPreenchimento();
        iconeVolume = criarIconeVolume();

        HBox layoutPrincipal = new HBox(0);
        layoutPrincipal.setAlignment(Pos.CENTER_LEFT);

        StackPane barraVolume = new StackPane();
        barraVolume.getChildren().addAll(barraFundo, barraPreenchimento);

        layoutPrincipal.getChildren().addAll(iconeVolume, barraVolume);

        this.getChildren().add(layoutPrincipal);
    }

    /**
     * @return Rectangle representando o fundo da barra de volume.
     */
    private Rectangle criarBarraFundo() {
        Rectangle fundo = new Rectangle(LARGURA_TOTAL, ALTURA_TOTAL);
        fundo.setFill(Color.web(COR_FUNDO_BARRA));
        fundo.setStroke(Color.DARKGRAY);
        fundo.setStrokeWidth(1);
        return fundo;
    }

    /**
     * @return Rectangle representando a barra de preenchimento.
     */
    private Rectangle criarBarraPreenchimento() {
        Rectangle preenchimento = new Rectangle(LARGURA_TOTAL * volumeAtual, ALTURA_TOTAL);
        preenchimento.setFill(Color.web(COR_PREENCHIMENTO_BARRA));
        StackPane.setAlignment(preenchimento, Pos.CENTER_LEFT);
        return preenchimento;
    }

    /**
     * @return ImageView do ícone de volume.
     */
    private ImageView criarIconeVolume() {
        ImageView icone = new ImageView();
        icone.setFitHeight(ALTURA_ICONE);
        icone.setFitWidth(LARGURA_ICONE);
        atualizarImagemIconeVolume();

        // Clique no ícone alterna entre mudo e volume máximo
        icone.setOnMouseClicked(event -> {
            event.consume();
            alternarMudo();
        });

        return icone;
    }

    /**
     * Configura eventos de clique e arrasto para ajuste do volume.
     */
    private void configurarEventosInterativos() {
        this.setOnMouseClicked(event -> ajustarVolumePelaPosicao(event.getX()));
        this.setOnMouseDragged(event -> ajustarVolumePelaPosicao(event.getX()));
    }

    private void atualizarVisualBarra() {
        barraPreenchimento.setWidth(LARGURA_TOTAL * volumeAtual);
        atualizarImagemIconeVolume();
    }

    private void atualizarImagemIconeVolume() {
        String caminhoIcone = obterCaminhoIcone();
        try {
            Image novaImagem = new Image(Objects.requireNonNull(getClass().getResource(caminhoIcone)).toExternalForm());
            iconeVolume.setImage(novaImagem);
        } catch (NullPointerException | IllegalArgumentException erro) {
            System.err.println("Erro ao carregar imagem do ícone de volume: " + caminhoIcone);
        }
    }

    /**
     * @return Caminho relativo da imagem do ícone de volume.
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

    public void alternarMudo() {
        if (volumeAtual > VOLUME_MINIMO) {
            definirVolume(VOLUME_MINIMO);
        } else {
            definirVolume(VOLUME_MAXIMO);
        }
    }

    /**
     * @param posicaoX Posição X do evento na barra.
     */
    private void ajustarVolumePelaPosicao(double posicaoX) {
        double novoVolume = Math.max(VOLUME_MINIMO, Math.min(VOLUME_MAXIMO, posicaoX / LARGURA_TOTAL));
        definirVolume(novoVolume);
    }

    /**
     * @param reprodutorMidia Instância de MediaPlayer do JavaFX.
     */
    public void setReprodutorMidia(MediaPlayer reprodutorMidia) {
        this.reprodutorMidia = reprodutorMidia;
        if (this.reprodutorMidia != null) {
            this.reprodutorMidia.setVolume(volumeAtual);
        }
    }

    /**
     * @param volume Valor do volume (0.0 a 1.0)
     */
    public void definirVolume(double volume) {
        this.volumeAtual = Math.max(VOLUME_MINIMO, Math.min(VOLUME_MAXIMO, volume));
        atualizarVisualBarra();

        if (reprodutorMidia != null) {
            reprodutorMidia.setVolume(this.volumeAtual);
        }
        gerenciadorPersistencia.salvarVolume(this.volumeAtual);
    }
}
