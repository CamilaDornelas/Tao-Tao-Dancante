package jogo.personagens;

import jogo.modelo.enume.TipoPersonagem;
import jogo.excecoes.PersonagemInvalidoException;
import jogo.excecoes.VidaInsuficienteException;
import javafx.scene.image.Image;
import java.util.Objects;


public class Lorde extends PersonagemJogo {
    
    private int pontosPrecisao;
    private boolean posturaElegante;
    
    private final Image imgPensador;
    private final Image imgRaiva;
    private final Image imgFeliz;
    
    public Lorde(double width, double height) throws PersonagemInvalidoException {
        super("Lorde", TipoPersonagem.LORDE, width, height);
        
        this.pontosPrecisao = 15;
        this.posturaElegante = true;
        
        imgPensador = new Image(Objects.requireNonNull(
            getClass().getResource("/assets/persona/lordThinker.png")).toExternalForm());
        imgRaiva = new Image(Objects.requireNonNull(
            getClass().getResource("/assets/persona/lordRaiva.png")).toExternalForm());
        imgFeliz = new Image(Objects.requireNonNull(
            getClass().getResource("/assets/persona/lordFeliz.png")).toExternalForm());
        
        this.setImage(imgPensador);
    }
    
    public void ficarPensativo() {
        this.setImage(imgPensador);
    }
    
    public void ficarComRaiva() {
        this.setImage(imgRaiva);
        this.posturaElegante = false;
    }
    
    public void ficarFeliz() {
        this.setImage(imgFeliz);
        this.posturaElegante = true;
    }
    

    @Override
    public void executarMovimento() {
        System.out.println(getNome() + " executa um movimento nobre e elegante!");
        
        int pontosBase = getTipo().getBonusPontuacao();
        if (posturaElegante) {
            pontosBase += pontosPrecisao;
        }
        
        adicionarPontos(pontosBase);
        ficarFeliz();
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
    

    public int getPontosPrecisao() { 
        return pontosPrecisao; 
    }
    
    public boolean isPosturaElegante() { 
        return posturaElegante; 
    }
}