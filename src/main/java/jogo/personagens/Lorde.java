package jogo.personagens;

import jogo.modelo.enume.TipoPersonagem;
import jogo.excecoes.PersonagemInvalidoException;
import jogo.excecoes.VidaInsuficienteException;
import javafx.scene.image.Image;
import java.util.Objects;

/**
 * Classe Lorde que herda de PersonagemJogo.
 * Mantém as funcionalidades visuais + adiciona lógica de gameplay.
 */
public class Lorde extends PersonagemJogo {
    
    // Atributos específicos do Lorde (para atender requisito de POO)
    private int pontosPrecisao;
    private boolean posturaElegante;
    
    // Suas imagens existentes
    private final Image imgPensador;
    private final Image imgRaiva;
    private final Image imgFeliz;
    
    public Lorde(double width, double height) throws PersonagemInvalidoException {
        super("Lorde", TipoPersonagem.LORDE, width, height);
        
        // Inicializa atributos específicos do Lorde
        this.pontosPrecisao = 15;
        this.posturaElegante = true;
        
        // Suas imagens (mantendo exatamente como estava)
        imgPensador = new Image(Objects.requireNonNull(
            getClass().getResource("/assets/persona/lordThinker.png")).toExternalForm());
        imgRaiva = new Image(Objects.requireNonNull(
            getClass().getResource("/assets/persona/lordRaiva.png")).toExternalForm());
        imgFeliz = new Image(Objects.requireNonNull(
            getClass().getResource("/assets/persona/lordFeliz.png")).toExternalForm());
        
        this.setImage(imgPensador);
    }
    
    // SEUS métodos visuais (mantidos exatamente como estavam)
    public void ficarPensativo() {
        this.setImage(imgPensador);
    }
    
    public void ficarComRaiva() {
        this.setImage(imgRaiva);
        this.posturaElegante = false; // Perde elegância quando fica com raiva
    }
    
    public void ficarFeliz() {
        this.setImage(imgFeliz);
        this.posturaElegante = true; // Recupera elegância quando fica feliz
    }
    
    // NOVOS métodos para atender requisitos de POO
    @Override
    public void executarMovimento() {
        System.out.println(getNome() + " executa um movimento nobre e elegante!");
        
        int pontosBase = getTipo().getBonusPontuacao();
        if (posturaElegante) {
            pontosBase += pontosPrecisao;
        }
        
        adicionarPontos(pontosBase);
        ficarFeliz(); // Muda visual quando executa movimento
    }
    
    @Override
    public void usarHabilidadeEspecial() {
        System.out.println(getNome() + " usa Refinamento Nobre!");
        this.pontosPrecisao += 10;
        this.posturaElegante = true;
        setMultiplicadorPontos(getMultiplicadorPontos() + 0.25);
        ficarPensativo(); // Muda visual para pensativo
    }
    
    @Override
    public String obterDescricao() {
        return "Lorde " + getNome() + " - Especialista em movimentos elegantes. " +
               "Precisão: " + pontosPrecisao + 
               (posturaElegante ? " (Postura Elegante)" : " (Postura Comprometida)");
    }
    
    // Getters específicos do Lorde
    public int getPontosPrecisao() { 
        return pontosPrecisao; 
    }
    
    public boolean isPosturaElegante() { 
        return posturaElegante; 
    }
}