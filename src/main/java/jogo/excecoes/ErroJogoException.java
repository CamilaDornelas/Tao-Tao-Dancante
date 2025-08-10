package jogo.excecoes;

/**
 * Exceção personalizada para representar erros durante o gameplay do jogo.
 */

public class ErroJogoException extends Exception {

    private final String faseAtual;
    private final String tipoErro;


    public ErroJogoException(String mensagem) {
        super("Erro no jogo: " + mensagem);
        this.faseAtual = null;
        this.tipoErro = null;
    }

    /**
     * Construtor para ErroJogoException com informações contextuais.
     */
    public ErroJogoException(String mensagem, String faseAtual, String tipoErro) {
        super("Erro no jogo: " + mensagem);
        this.faseAtual = faseAtual;
        this.tipoErro = tipoErro;
    }

    /**
     * Construtor para ErroJogoException que encapsula uma exceção subjacente (causa).
     */
    public ErroJogoException(String mensagem, Throwable causa) {
        super("Erro no jogo: " + mensagem, causa);
        this.faseAtual = null;
        this.tipoErro = null;
    }


    public String getFaseAtual() {
        return faseAtual;
    }


    public String getTipoErro() {
        return tipoErro;
    }

    @Override
    public String getMessage() {
        StringBuilder mensagemCompleta = new StringBuilder(super.getMessage());
        if (faseAtual != null) {
            mensagemCompleta.append(" - Fase: ").append(faseAtual);
        }
        if (tipoErro != null) {
            mensagemCompleta.append(" - Tipo: ").append(tipoErro);
        }
        return mensagemCompleta.toString();
    }
}