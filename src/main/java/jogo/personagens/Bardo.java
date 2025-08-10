package jogo.personagens;

import jogo.modelo.enume.TipoPersonagem;
import jogo.excecoes.PersonagemInvalidoException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * Classe Bardo que mantém sua animação + adiciona lógica de jogo.
 */
public class Bardo extends PersonagemJogo {
    
    // Atributos específicos do Bardo
    private int bonusRitmo;
    private boolean inspiracaoAtiva;
    
    // Suas animações existentes
    private final Image png1;
    private final Image png2;
    private final Timeline animacoes;
    
    public Bardo(double width, double height) throws PersonagemInvalidoException {
        super("Bardo", TipoPersonagem.BARDO, width, height);
        
        // Inicializa atributos específicos
        this.bonusRitmo = 10;
        this.inspiracaoAtiva = false;
        
        // Suas animações (mantendo exatamente como estava)
        this.png1 = new Image(getClass().getResource("/assets/persona/bardoDance1.png").toExternalForm());
        this.png2 = new Image(getClass().getResource("/assets/persona/bardoDance2.png").toExternalForm());
        
        this.setImage(png1);
        
        animacoes = new Timeline(
            new KeyFrame(Duration.millis(400), e -> toggleFrame())
        );
        animacoes.setCycleCount(Timeline.INDEFINITE);
        animacoes.play();
    }
    
    // SEU método original (mantido)
    private void toggleFrame() {
        if (this.getImage() == png1) {
            this.setImage(png2);
        } else {
            this.setImage(png1);
        }
    }
    
    // SEU getter original (mantido)
    public Timeline getAnimacao() {
        return animacoes;
    }
    
    // NOVOS métodos para POO
    @Override
    public void executarMovimento() {
        System.out.println(getNome() + " executa um movimento musical ritmado!");
        
        int pontosBase = getTipo().getBonusPontuacao();
        int pontosComBonus = pontosBase + bonusRitmo;
        
        if (inspiracaoAtiva) {
            pontosComBonus *= 2;
            inspiracaoAtiva = false;
        }
        
        adicionarPontos(pontosComBonus);
        // A animação continua automaticamente
    }
    
    @Override
    public void usarHabilidadeEspecial() {
        System.out.println(getNome() + " usa Inspiração Bardica!");
        this.inspiracaoAtiva = true;
        this.bonusRitmo += 5;
        setMultiplicadorPontos(getMultiplicadorPontos() + 0.2);
        
        // Pode acelerar a animação temporariamente
        animacoes.setRate(1.5);
    }
    
    @Override
    public String obterDescricao() {
        return "Bardo " + getNome() + " - Especialista em ritmo e música. " +
               "Bônus de ritmo: " + bonusRitmo + 
               (inspiracaoAtiva ? " (Inspiração Ativa!)" : "");
    }
}