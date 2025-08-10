package jogo.componentes;

import javafx.animation.FadeTransition;
import javafx.util.Duration;

/**
 * Seta especial que herda de Setas e adiciona efeito visual quando acertada
 * Demonstra herança simples e polimorfismo
 */
public class SetaEspecial extends Setas {
    
    public SetaEspecial(TipoSetas tipo, double largura, double altura, Runnable missAction) {
        super(tipo, largura, altura, missAction);
    }
    
    /**
     * Sobrescreve o método da classe pai para adicionar efeito especial
     */
    @Override
    public void aplicarEfeitoBrilho() {
        super.aplicarEfeitoBrilho();
        
        this.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.7, 0, 0);");
        
        FadeTransition fade = new FadeTransition(Duration.millis(300), this);
        fade.setFromValue(0.8);
        fade.setToValue(1.0);
        fade.setCycleCount(2);
        fade.setAutoReverse(true);
        fade.play();
    }
}
