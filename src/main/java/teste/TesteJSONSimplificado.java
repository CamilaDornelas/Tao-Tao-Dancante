package teste;

import jogo.servicos.LeitorJSONSimples;

public class TesteJSONSimplificado {
    public static void main(String[] args) {
        System.out.println("🎯 TESTE DO NOVO SISTEMA SIMPLIFICADO");
        System.out.println("=" .repeat(50));
        System.out.println("✨ Agora não precisamos mais do DadosFase!");
        System.out.println("📖 Lendo JSON DIRETAMENTE!\n");
        
        // Testa carregamento direto da Fase 1
        System.out.println("🎮 FASE 1:");
        
        // Carrega sequência de setas
        int[] setas = LeitorJSONSimples.carregarSequenciaSetas(1);
        System.out.println("🎯 Sequência de setas: " + setas.length + " setas");
        
        // Mostra as primeiras 10 setas
        System.out.print("🎼 Primeiras 10: ");
        String[] nomes = {"⬅️", "⬆️", "⬇️", "➡️"};
        for (int i = 0; i < Math.min(10, setas.length); i++) {
            if (setas[i] >= 0 && setas[i] < 4) {
                System.out.print(nomes[setas[i]] + " ");
            }
        }
        System.out.println();
        
        // Carrega caminho da música
        String musica = LeitorJSONSimples.carregarCaminhoMusica(1);
        System.out.println("🎵 Música: " + musica);
        
        // Carrega configurações de tempo
        LeitorJSONSimples.ConfiguracoesTempo config = 
            LeitorJSONSimples.carregarConfiguracoesTempo(1);
        System.out.println("⏱️ Configurações:");
        System.out.println("   Duração inicial: " + config.duracaoInicial + "ms");
        System.out.println("   Duração final: " + config.duracaoFinal + "ms");
        System.out.println("   Tempo aceleração: " + config.tempoAceleracao + "ms");
        
        System.out.println("\n🎉 SUCESSO! Dados carregados DIRETAMENTE do JSON!");
        System.out.println("💡 Agora o fluxo é: JSON → LeitorJSONSimples → GerenciadorSetas");
        System.out.println("❌ Sem mais: JSON → LeitorDadosJSON → DadosFase → GerenciadorSetas");
    }
}
