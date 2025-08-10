package jogo.servicos;

import java.util.prefs.Preferences;

/**
 * Gerenciador de persistência para configurações de volume
 * Salva/carrega configurações usando a API Preferences do Java (no Sistema Operacional)
 */
public class GerenciadorPersistenciaVolume {
    
    private static final String CHAVE_VOLUME = "volume_jogo";
    private static final double VOLUME_PADRAO = 1.0; // ✨ MUDANÇA: Volume inicial 100%
    
    // Preferences do usuário para este aplicativo
    private final Preferences prefs;
    
    public GerenciadorPersistenciaVolume() {
        // Cria um nó de preferências específico para nosso jogo
        this.prefs = Preferences.userNodeForPackage(GerenciadorPersistenciaVolume.class);
    }
    
    /**
     * Salva o volume atual no Sistema Operacional usando Preferences API
     */
    public void salvarVolume(double volume) {
        try {
            // Salva no registry/preferences do SO
            prefs.putDouble(CHAVE_VOLUME, volume);
            
            // Força a gravação imediata (flush)
            prefs.flush();
            
            System.out.println("💾 Volume salvo no SO: " + (int)(volume * 100) + "%");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar volume no SO: " + e.getMessage());
        }
    }
    
    /**
     * Carrega o volume salvo do Sistema Operacional
     */
    public double carregarVolume() {
        try {
            // Busca no registry/preferences do SO
            double volume = prefs.getDouble(CHAVE_VOLUME, VOLUME_PADRAO);
            
            // Garante que está no range válido
            volume = Math.max(0, Math.min(1, volume));
            
            System.out.println("📂 Volume carregado do SO: " + (int)(volume * 100) + "%");
            return volume;
            
        } catch (Exception e) {
            System.out.println("⚠️  Erro ao carregar do SO, usando volume padrão: " + (int)(VOLUME_PADRAO * 100) + "%");
            return VOLUME_PADRAO;
        }
    }
    
    /**
     * Reseta as configurações para o padrão no Sistema Operacional
     */
    public void resetarConfiguracoes() {
        try {
            salvarVolume(VOLUME_PADRAO);
            System.out.println("� Configurações de volume resetadas no SO");
        } catch (Exception e) {
            System.err.println("❌ Erro ao resetar configurações: " + e.getMessage());
        }
    }
    
    /**
     * Remove completamente as configurações do Sistema Operacional
     */
    public void limparConfiguracoes() {
        try {
            prefs.remove(CHAVE_VOLUME);
            prefs.flush();
            System.out.println("�️  Configurações de volume removidas do SO");
        } catch (Exception e) {
            System.err.println("❌ Erro ao limpar configurações: " + e.getMessage());
        }
    }
}
