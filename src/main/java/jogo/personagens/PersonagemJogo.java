package jogo.personagens;

import jogo.modelo.enume.TipoPersonagem;
import jogo.excecoes.VidaInsuficienteException;
import jogo.excecoes.PersonagemInvalidoException;

/**
 * Classe que estende sua classe Personagem original e adiciona lógica de jogo.
 * Combina funcionalidades visuais (JavaFX) com lógica de gameplay (POO).
 */
public abstract class PersonagemJogo extends Personagem {
    
    // Atributos para lógica de jogo (POO)
    protected String nome;
    protected TipoPersonagem tipo;
    protected int pontuacao;
    protected int vidas;
    protected boolean ativo;
    protected double multiplicadorPontos;
    
    public PersonagemJogo(String nome, TipoPersonagem tipo, double width, double height) throws PersonagemInvalidoException {
        super(width, height); // Chama construtor da sua classe original
        
        // Validações com exceções
        if (nome == null || nome.trim().isEmpty()) {
            throw new PersonagemInvalidoException("Nome não pode ser vazio", tipo.toString(), "Nome inválido");
        }
        if (tipo == null) {
            throw new PersonagemInvalidoException("Tipo não pode ser nulo", "DESCONHECIDO", "Tipo nulo");
        }
        if (width <= 0 || height <= 0) {
            throw new PersonagemInvalidoException("Dimensões devem ser positivas", tipo.toString(), "Dimensões inválidas");
        }
        
        this.nome = nome;
        this.tipo = tipo;
        this.pontuacao = 0;
        this.vidas = 3;
        this.ativo = true;
        this.multiplicadorPontos = 1.0;
    }
    
    // Métodos abstratos (para POO)
    public abstract void executarMovimento();
    public abstract void usarHabilidadeEspecial();
    public abstract String obterDescricao();
    
    // Métodos de lógica de jogo
    public void adicionarPontos(int pontos) {
        if (pontos > 0) {
            this.pontuacao += (int) (pontos * multiplicadorPontos);
        }
    }
    
    public boolean perderVida() throws VidaInsuficienteException {
        if (this.vidas <= 0) {
            throw new VidaInsuficienteException("Personagem já morreu", 0, "perder vida");
        }
        
        this.vidas--;
        if (this.vidas <= 0) {
            this.ativo = false;
            return false;
        }
        return true;
    }
    
    /**
     * Método para usar habilidade que requer vida
     */
    public void usarHabilidadeComVida(String habilidade) throws VidaInsuficienteException {
        if (this.vidas <= 1) {
            throw new VidaInsuficienteException("Vida insuficiente para usar habilidade", this.vidas, habilidade);
        }
        // Usar habilidade custaria 1 vida
        this.vidas--;
    }
    
    // Getters e Setters
    public String getNome() { return nome; }
    public TipoPersonagem getTipo() { return tipo; }
    public int getPontuacao() { return pontuacao; }
    public int getVidas() { return vidas; }
    public boolean isAtivo() { return ativo; }
    public double getMultiplicadorPontos() { return multiplicadorPontos; }
    
    protected void setMultiplicadorPontos(double multiplicador) {
        this.multiplicadorPontos = Math.max(1.0, multiplicador);
    }
}