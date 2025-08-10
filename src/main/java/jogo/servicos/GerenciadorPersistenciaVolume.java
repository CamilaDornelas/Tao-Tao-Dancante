package jogo.servicos;

import java.util.prefs.Preferences;

/**
 * Gerenciador de persistência para configurações de volume
 * Salva/carrega configurações usando a API Preferences do Java (no Sistema Operacional)
 */
public class GerenciadorPersistenciaVolume {
    
    private static final String CHAVE_VOLUME = "volume_jogo";
    private static final double VOLUME_PADRAO = 1.0; // ✨ MUDANÇA: Volume inicial 100%
    
    private final Preferences prefs;
    
    public GerenciadorPersistenciaVolume() {
        this.prefs = Preferences.userNodeForPackage(GerenciadorPersistenciaVolume.class);
    }
    

    public void salvarVolume(double volume) {
        try {
            prefs.putDouble(CHAVE_VOLUME, volume);
            
            prefs.flush();
            
            System.out.println("💾 Volume salvo no SO: " + (int)(volume * 100) + "%");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar volume no SO: " + e.getMessage());
        }
    }
    

    public double carregarVolume() {
        try {
            double volume = prefs.getDouble(CHAVE_VOLUME, VOLUME_PADRAO);
            
            volume = Math.max(0, Math.min(1, volume));
            
            System.out.println("📂 Volume carregado do SO: " + (int)(volume * 100) + "%");
            return volume;
            
        } catch (Exception e) {
            System.out.println("⚠️  Erro ao carregar do SO, usando volume padrão: " + (int)(VOLUME_PADRAO * 100) + "%");
            return VOLUME_PADRAO;
        }
    }
    

    public void resetarConfiguracoes() {
        try {
            salvarVolume(VOLUME_PADRAO);
            System.out.println("� Configurações de volume resetadas no SO");
        } catch (Exception e) {
            System.err.println("❌ Erro ao resetar configurações: " + e.getMessage());
        }
    }
    

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
