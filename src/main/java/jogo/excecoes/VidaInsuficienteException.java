package jogo.excecoes;

/**
 * Exceção lançada quando há problemas com vidas do personagem
 * RuntimeException para erros que podem ser evitados
 */
public class VidaInsuficienteException extends RuntimeException {
    
    private int vidasRestantes;
    private String acaoTentada;
    
    public VidaInsuficienteException(String mensagem) {
        super("Vidas insuficientes: " + mensagem);
    }
    
    public VidaInsuficienteException(String mensagem, int vidasRestantes) {
        super("Vidas insuficientes: " + mensagem);
        this.vidasRestantes = vidasRestantes;
    }
    
    public VidaInsuficienteException(String mensagem, int vidasRestantes, String acaoTentada) {
        super("Vidas insuficientes: " + mensagem);
        this.vidasRestantes = vidasRestantes;
        this.acaoTentada = acaoTentada;
    }
    
    public int getVidasRestantes() {
        return vidasRestantes;
    }
    
    public String getAcaoTentada() {
        return acaoTentada;
    }
    
    @Override
    public String getMessage() {
        StringBuilder msg = new StringBuilder(super.getMessage());
        if (vidasRestantes >= 0) {
            msg.append(" - Vidas restantes: ").append(vidasRestantes);
        }
        if (acaoTentada != null) {
            msg.append(" - Ação tentada: ").append(acaoTentada);
        }
        return msg.toString();
    }
}
