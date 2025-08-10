package jogo.modelo;

/**
 * ✨ DADOS ÚNICOS E IMUTÁVEIS DE CADA FASE
 * Cada fase tem características específicas que são salvas em arquivos
 * e NÃO PODEM ser reutilizadas entre fases diferentes
 */
public class DadosFase {
    
    private final int numeroFase;
    private final int[] sequenciaSetas;
    private final String caminhoMusica;
    private final double pesoPontuacao;
    private final String imagemBardo;
    private final String imagemLorde;
    private final String imagemBackground;
    private final double duracaoSetasInicial;
    private final double duracaoSetasFinal;
    private final double tempoAceleracao;
    private final String nomeFase;
    private final String dificuldade;
    
    /**
     * Construtor privado - dados só podem ser criados via métodos estáticos
     */
    private DadosFase(int numeroFase, int[] sequenciaSetas, String caminhoMusica, 
                     double pesoPontuacao, String imagemBardo, String imagemLorde,
                     String imagemBackground, double duracaoSetasInicial, 
                     double duracaoSetasFinal, double tempoAceleracao,
                     String nomeFase, String dificuldade) {
        this.numeroFase = numeroFase;
        this.sequenciaSetas = sequenciaSetas.clone(); // Cópia defensiva
        this.caminhoMusica = caminhoMusica;
        this.pesoPontuacao = pesoPontuacao;
        this.imagemBardo = imagemBardo;
        this.imagemLorde = imagemLorde;
        this.imagemBackground = imagemBackground;
        this.duracaoSetasInicial = duracaoSetasInicial;
        this.duracaoSetasFinal = duracaoSetasFinal;
        this.tempoAceleracao = tempoAceleracao;
        this.nomeFase = nomeFase;
        this.dificuldade = dificuldade;
    }
    
    /**
     * ✨ NOVO: Cria DadosFase a partir de dados do JSON
     * Este método permite criar uma fase usando dados externos (do JSON)
     */
    public static DadosFase criarDoJSON(int numeroFase, String nomeFase, String dificuldade,
                                       double pesoPontuacao, int[] sequenciaSetas, 
                                       String caminhoMusica, String imagemBardo, 
                                       String imagemLorde, String imagemBackground,
                                       double duracaoSetasInicial, double duracaoSetasFinal, 
                                       double tempoAceleracao) {
        return new DadosFase(numeroFase, sequenciaSetas, caminhoMusica, pesoPontuacao,
                           imagemBardo, imagemLorde, imagemBackground, 
                           duracaoSetasInicial, duracaoSetasFinal, tempoAceleracao,
                           nomeFase, dificuldade);
    }
    
    // ====== GETTERS (todos imutáveis) ======
    
    public int getNumeroFase() { return numeroFase; }
    
    /**
     * Retorna cópia da sequência para evitar modificação
     */
    public int[] getSequenciaSetas() { return sequenciaSetas.clone(); }
    
    public String getCaminhoMusica() { return caminhoMusica; }
    public double getPesoPontuacao() { return pesoPontuacao; }
    public String getImagemBardo() { return imagemBardo; }
    public String getImagemLorde() { return imagemLorde; }
    public String getImagemBackground() { return imagemBackground; }
    public double getDuracaoSetasInicial() { return duracaoSetasInicial; }
    public double getDuracaoSetasFinal() { return duracaoSetasFinal; }
    public double getTempoAceleracao() { return tempoAceleracao; }
    public String getNomeFase() { return nomeFase; }
    public String getDificuldade() { return dificuldade; }
    
    /**
     * Calcula duração da seta baseado no tempo atual da música
     */
    public double calcularDuracaoSeta(double tempoAtualMusica) {
        if (tempoAtualMusica >= tempoAceleracao) {
            return duracaoSetasFinal;
        }
        
        double progresso = Math.min(1.0, tempoAtualMusica / tempoAceleracao);
        return duracaoSetasInicial - ((duracaoSetasInicial - duracaoSetasFinal) * progresso);
    }
    
    /**
     * Calcula pontuação considerando o peso da fase
     */
    public int calcularPontuacao(int pontuacaoBase) {
        return (int)(pontuacaoBase * pesoPontuacao);
    }
    
    /**
     * Retorna informações resumidas da fase
     */
    @Override
    public String toString() {
        return String.format("Fase %d: %s [%s] - %d setas, %.1fx pontos", 
            numeroFase, nomeFase, dificuldade, sequenciaSetas.length, pesoPontuacao);
    }
}
