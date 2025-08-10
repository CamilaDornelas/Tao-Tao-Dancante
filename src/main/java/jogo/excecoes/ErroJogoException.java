package jogo.excecoes;

/**
 * Exceção para erros no jogo (gameplay)
 * Demonstra hierarquia de exceções
 */
public class ErroJogoException extends Exception {
    
    private String faseAtual;
    private String tipoErro;
    
    public ErroJogoException(String mensagem) {
        super("Erro no jogo: " + mensagem);
    }
    
    public ErroJogoException(String mensagem, String faseAtual, String tipoErro) {
        super("Erro no jogo: " + mensagem);
        this.faseAtual = faseAtual;
        this.tipoErro = tipoErro;
    }
    
    public ErroJogoException(String mensagem, Throwable causa) {
        super("Erro no jogo: " + mensagem, causa);
    }
    
    public String getFaseAtual() {
        return faseAtual;
    }
    
    public String getTipoErro() {
        return tipoErro;
    }
    
    @Override
    public String getMessage() {
        StringBuilder msg = new StringBuilder(super.getMessage());
        if (faseAtual != null) {
            msg.append(" - Fase: ").append(faseAtual);
        }
        if (tipoErro != null) {
            msg.append(" - Tipo: ").append(tipoErro);
        }
        return msg.toString();
    }
}
