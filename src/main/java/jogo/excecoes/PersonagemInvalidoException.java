package jogo.excecoes;

/**
 * Exceção lançada quando um personagem é inválido
 * Demonstra conceito de exceções customizadas para POO
 */
public class PersonagemInvalidoException extends Exception {
    
    private String tipoPersonagem;
    private String motivoErro;
    
    public PersonagemInvalidoException(String mensagem) {
        super("Erro de personagem: " + mensagem);
    }
    
    public PersonagemInvalidoException(String mensagem, String tipoPersonagem) {
        super("Erro de personagem: " + mensagem);
        this.tipoPersonagem = tipoPersonagem;
    }
    
    public PersonagemInvalidoException(String mensagem, String tipoPersonagem, String motivoErro) {
        super("Erro de personagem: " + mensagem);
        this.tipoPersonagem = tipoPersonagem;
        this.motivoErro = motivoErro;
    }
    
    public String getTipoPersonagem() {
        return tipoPersonagem;
    }
    
    public String getMotivoErro() {
        return motivoErro;
    }
    
    @Override
    public String getMessage() {
        StringBuilder msg = new StringBuilder(super.getMessage());
        if (tipoPersonagem != null) {
            msg.append(" - Tipo: ").append(tipoPersonagem);
        }
        if (motivoErro != null) {
            msg.append(" - Motivo: ").append(motivoErro);
        }
        return msg.toString();
    }
}
