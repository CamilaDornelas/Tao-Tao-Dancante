package jogo.componentes;

import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class SetaEspecial extends Setas {

    private static final double OPACIDADE_INICIAL_BRILHO = 0.8;
    private static final double OPACIDADE_FINAL_BRILHO = 1.0;
    private static final int CICLOS_BRILHO = 2;
    private static final int DURACAO_BRILHO_MILLIS = 300;
    private static final String ESTILO_BRILHO_ESPECIAL = "-fx-effect: dropshadow(gaussian, gold, 10, 0.7, 0, 0);";

    /**
     * @param tipo Tipo da seta (ex.: cima, baixo, esquerda, direita).
     * @param largura Largura visual da seta em pixels.
     * @param altura Altura visual da seta em pixels.
     * @param acaoErro Runnable executado quando o jogador erra a seta.
     */
    public SetaEspecial(TipoSetas tipo, double largura, double altura, Runnable acaoErro) {
        super(tipo, largura, altura, acaoErro);
    }

    @Override
    public void aplicarEfeitoBrilho() {
        // Aplica efeitos base da classe Setas
        super.aplicarEfeitoBrilho();

        // Aplica estilo de brilho especial
        this.setStyle(ESTILO_BRILHO_ESPECIAL);

        // Cria a animação de fade para o brilho
        FadeTransition transicaoBrilho = new FadeTransition(Duration.millis(DURACAO_BRILHO_MILLIS), this);
        transicaoBrilho.setFromValue(OPACIDADE_INICIAL_BRILHO);
        transicaoBrilho.setToValue(OPACIDADE_FINAL_BRILHO);
        transicaoBrilho.setCycleCount(CICLOS_BRILHO);
        transicaoBrilho.setAutoReverse(true);
        transicaoBrilho.play();
    }
}
