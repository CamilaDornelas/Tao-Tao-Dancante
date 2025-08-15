package jogo.componentes;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.util.List;
import java.util.Objects;
import java.lang.Runnable;

public class Setas extends ImageView {

    public enum TipoSetas {
        UP("/assets/setas/cima.png"),
        DOWN("/assets/setas/baixo.png"),
        LEFT("/assets/setas/esquerda.png"),
        RIGHT("/assets/setas/direita.png");

        private final String caminhoImagem;

        TipoSetas(String caminhoImagem) {
            this.caminhoImagem = caminhoImagem;
        }

        public String getCaminhoImagem() {
            return caminhoImagem;
        }
    }

    private static final double OPACIDADE_INICIAL_ANIMACAO = 0.0;
    private static final double OPACIDADE_FINAL_ANIMACAO = 1.0;
    private static final double OPACIDADE_BRILHO_FINAL = 1.1;
    private static final double DURACAO_FLASH_SEGUNDOS = 0.15;
    private static final String ESTILO_BRILHO = "-fx-effect: dropshadow(gaussian, gold, 5, 0.4, 0, 0);";

    private TipoSetas tipo;
    private ParallelTransition animacaoSubida;
    private final Runnable acaoErro;

    public Setas(TipoSetas tipo, double largura, double altura, Runnable acaoErro) {
        this.tipo = tipo;
        this.acaoErro = acaoErro;
        setFitWidth(largura);
        setFitHeight(altura);
        setImage(new Image(Objects.requireNonNull(getClass().getResource(tipo.getCaminhoImagem())).toExternalForm()));
        setOpacity(OPACIDADE_INICIAL_ANIMACAO);
    }

    public TipoSetas getTipo() {
        return tipo;
    }

    public void setTipo(TipoSetas tipo) {
        this.tipo = tipo;
        setImage(new Image(Objects.requireNonNull(getClass().getResource(tipo.getCaminhoImagem())).toExternalForm()));
    }

    public ParallelTransition iniciarAnimacaoSubida(double duracao, double distancia) {
        FadeTransition transicaoFade = new FadeTransition(Duration.millis(duracao), this);
        transicaoFade.setFromValue(OPACIDADE_INICIAL_ANIMACAO);
        transicaoFade.setToValue(OPACIDADE_FINAL_ANIMACAO);

        TranslateTransition transicaoTranslacao = new TranslateTransition(Duration.millis(duracao), this);
        transicaoTranslacao.setByY(-distancia);

        animacaoSubida = new ParallelTransition(transicaoFade, transicaoTranslacao);
        animacaoSubida.play();
        return animacaoSubida;
    }

    public void esconder() {
        this.setVisible(false);
        if (animacaoSubida != null && animacaoSubida.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            animacaoSubida.stop();
        }
    }

    public void lidarComErro(AnchorPane painelPai, List<Setas> setasAtivas) {
        if (painelPai.getChildren().contains(this)) {
            painelPai.getChildren().remove(this);
        }
        if (setasAtivas.contains(this)) {
            setasAtivas.remove(this);
        }

        if (acaoErro != null) {
            acaoErro.run();
        }
    }

    public void mostrar() {
        this.setVisible(true);
        this.setOpacity(OPACIDADE_FINAL_ANIMACAO);
    }

    public ParallelTransition getAnimacaoSubida() {
        return animacaoSubida;
    }

    public void aplicarEfeitoBrilho() {
        this.setStyle(ESTILO_BRILHO);

        FadeTransition flash = new FadeTransition();
        flash.setNode(this);
        flash.setDuration(Duration.seconds(DURACAO_FLASH_SEGUNDOS));
        flash.setFromValue(OPACIDADE_FINAL_ANIMACAO);
        flash.setToValue(OPACIDADE_BRILHO_FINAL);
        flash.setCycleCount(1);
        flash.setAutoReverse(false);
        flash.play();
    }
}