package jogo.modelo;

/**
 * ✨ DADOS ÚNICOS E IMUTÁVEIS DE CADA FASE
 */
public final class DadosFase {

    private final int numeroFase;
    private final String nomeFase;
    private final String dificuldade;
    private final double pesoPontuacao;
    private final int[] sequenciaSetas;
    private final String caminhoMusica;
    private final String imagemBardo;
    private final String imagemLorde;
    private final String imagemBackground;
    private final double duracaoSetasInicial;
    private final double duracaoSetasFinal;
    private final double tempoAceleracao;


    private DadosFase(int numeroFase, String nomeFase, String dificuldade,
                      double pesoPontuacao, int[] sequenciaSetas, String caminhoMusica,
                      String imagemBardo, String imagemLorde, String imagemBackground,
                      double duracaoSetasInicial, double duracaoSetasFinal,
                      double tempoAceleracao) {
        this.numeroFase = numeroFase;
        this.nomeFase = nomeFase;
        this.dificuldade = dificuldade;
        this.pesoPontuacao = pesoPontuacao;
        this.sequenciaSetas = sequenciaSetas.clone(); // Cópia defensiva
        this.caminhoMusica = caminhoMusica;
        this.imagemBardo = imagemBardo;
        this.imagemLorde = imagemLorde;
        this.imagemBackground = imagemBackground;
        this.duracaoSetasInicial = duracaoSetasInicial;
        this.duracaoSetasFinal = duracaoSetasFinal;
        this.tempoAceleracao = tempoAceleracao;
    }


    public static DadosFase criarDoJSON(int numeroFase, String nomeFase, String dificuldade,
                                        double pesoPontuacao, int[] sequenciaSetas,
                                        String caminhoMusica, String imagemBardo,
                                        String imagemLorde, String imagemBackground,
                                        double duracaoSetasInicial, double duracaoSetasFinal,
                                        double tempoAceleracao) {
        return new DadosFase(numeroFase, nomeFase, dificuldade, pesoPontuacao,
                sequenciaSetas, caminhoMusica, imagemBardo,
                imagemLorde, imagemBackground, duracaoSetasInicial,
                duracaoSetasFinal, tempoAceleracao);
    }

    // ====== GETTERS (todos imutáveis) ======

    public int getNumeroFase() { return numeroFase; }
    public String getNomeFase() { return nomeFase; }
    public String getDificuldade() { return dificuldade; }
    public double getPesoPontuacao() { return pesoPontuacao; }


    public int[] getSequenciaSetas() { return sequenciaSetas.clone(); }

    public String getCaminhoMusica() { return caminhoMusica; }
    public String getImagemBardo() { return imagemBardo; }
    public String getImagemLorde() { return imagemLorde; }
    public String getImagemBackground() { return imagemBackground; }
    public double getDuracaoSetasInicial() { return duracaoSetasInicial; }
    public double getDuracaoSetasFinal() { return duracaoSetasFinal; }
    public double getTempoAceleracao() { return tempoAceleracao; }


    public double calcularDuracaoSeta(double tempoAtualMusica) {
        if (tempoAtualMusica >= tempoAceleracao) {
            return duracaoSetasFinal;
        }

        double progresso = Math.min(1.0, tempoAtualMusica / tempoAceleracao);
        return duracaoSetasInicial - ((duracaoSetasInicial - duracaoSetasFinal) * progresso);
    }


    public int calcularPontuacao(int pontuacaoBase) {
        return (int)(pontuacaoBase * pesoPontuacao);
    }


    @Override
    public String toString() {
        return String.format("Fase %d: %s [%s] - %d setas, %.1fx pontos",
                numeroFase, nomeFase, dificuldade, sequenciaSetas.length, pesoPontuacao);
    }
}