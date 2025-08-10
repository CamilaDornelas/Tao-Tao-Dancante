package jogo.modelo.enume;

public enum TipoPersonagem {
    BARDO("Bardo", "Personagem músico com ritmo especial", 15),
    FADA("Fada", "Personagem mágico e gracioso", 12),
    LORDE("Lorde", "Personagem nobre e elegante", 10),
    PRINCIPE("Príncipe", "Personagem real com técnicas avançadas", 20);
    
    private final String nome;
    private final String descricao;
    private final int bonusPontuacao;
    
    TipoPersonagem(String nome, String descricao, int bonusPontuacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.bonusPontuacao = bonusPontuacao;
    }
    
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public int getBonusPontuacao() { return bonusPontuacao; }
}