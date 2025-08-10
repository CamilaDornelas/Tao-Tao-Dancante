package jogo.servicos;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * ✨ LEITOR SIMPLES DE JSON - SEM CLASSES INTERMEDIÁRIAS
 * Lê diretamente do JSON e retorna os dados prontos para uso
 */
public class LeitorJSONSimples {
    
    private static final String PASTA_JSON = "/dados-fase/";
    
    /**
     * 🎯 CARREGA SEQUÊNCIA DE SETAS DIRETAMENTE DO JSON
     */
    public static int[] carregarSequenciaSetas(int numeroFase) {
        String nomeArquivo = "fase" + numeroFase + ".json";
        
        try {
            InputStream inputStream = LeitorJSONSimples.class.getResourceAsStream(PASTA_JSON + nomeArquivo);
            if (inputStream == null) {
                System.err.println("❌ Arquivo não encontrado: " + nomeArquivo);
                return new int[0]; // Array vazio
            }
            
            String json = lerInputStream(inputStream);
            return extrairSequenciaSetas(json);
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar sequência da fase " + numeroFase + ": " + e.getMessage());
            return new int[0];
        }
    }
    
    /**
     * 🎵 CARREGA CAMINHO DA MÚSICA DIRETAMENTE DO JSON
     */
    public static String carregarCaminhoMusica(int numeroFase) {
        String nomeArquivo = "fase" + numeroFase + ".json";
        
        try {
            InputStream inputStream = LeitorJSONSimples.class.getResourceAsStream(PASTA_JSON + nomeArquivo);
            if (inputStream == null) {
                return "/assets/musica/song1.mp3"; // Fallback
            }
            
            String json = lerInputStream(inputStream);
            return extrairCaminhoMusica(json);
            
        } catch (Exception e) {
            return "/assets/musica/song1.mp3"; // Fallback
        }
    }
    
    /**
     * 🎭 CARREGA CONFIGURAÇÕES DE TEMPO DIRETAMENTE DO JSON
     */
    public static ConfiguracoesTempo carregarConfiguracoesTempo(int numeroFase) {
        String nomeArquivo = "fase" + numeroFase + ".json";
        
        try {
            InputStream inputStream = LeitorJSONSimples.class.getResourceAsStream(PASTA_JSON + nomeArquivo);
            if (inputStream == null) {
                return new ConfiguracoesTempo(6000.0, 3000.0, 28000.0); // Fallback
            }
            
            String json = lerInputStream(inputStream);
            return extrairConfiguracoesTempo(json);
            
        } catch (Exception e) {
            return new ConfiguracoesTempo(6000.0, 3000.0, 28000.0); // Fallback
        }
    }
    
    // ===== MÉTODOS AUXILIARES =====
    
    private static String lerInputStream(InputStream inputStream) throws Exception {
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").next();
        }
    }
    
    private static int[] extrairSequenciaSetas(String json) {
        // Procura por "sequenciaSetas": [...]
        String busca = "\"sequenciaSetas\"";
        int inicio = json.indexOf(busca);
        if (inicio == -1) return new int[0];
        
        int inicioArray = json.indexOf('[', inicio);
        int fimArray = json.indexOf(']', inicioArray);
        if (inicioArray == -1 || fimArray == -1) return new int[0];
        
        String arrayStr = json.substring(inicioArray + 1, fimArray);
        String[] numeros = arrayStr.split(",");
        
        int[] setas = new int[numeros.length];
        for (int i = 0; i < numeros.length; i++) {
            setas[i] = Integer.parseInt(numeros[i].trim());
        }
        
        return setas;
    }
    
    private static String extrairCaminhoMusica(String json) {
        return extrairString(json, "caminhoMusica");
    }
    
    private static String extrairString(String json, String campo) {
        String busca = "\"" + campo + "\"";
        int inicio = json.indexOf(busca);
        if (inicio == -1) return "";
        
        int inicioValor = json.indexOf('"', inicio + busca.length() + 1);
        int fimValor = json.indexOf('"', inicioValor + 1);
        
        if (inicioValor == -1 || fimValor == -1) return "";
        return json.substring(inicioValor + 1, fimValor);
    }
    
    private static ConfiguracoesTempo extrairConfiguracoesTempo(String json) {
        double inicial = extrairDouble(json, "duracaoSetasInicial");
        double final_ = extrairDouble(json, "duracaoSetasFinal");
        double aceleracao = extrairDouble(json, "tempoAceleracao");
        
        return new ConfiguracoesTempo(inicial, final_, aceleracao);
    }
    
    private static double extrairDouble(String json, String campo) {
        String busca = "\"" + campo + "\"";
        int inicio = json.indexOf(busca);
        if (inicio == -1) return 0.0;
        
        int inicioValor = json.indexOf(':', inicio) + 1;
        int fimValor = json.indexOf(',', inicioValor);
        if (fimValor == -1) fimValor = json.indexOf('\n', inicioValor);
        if (fimValor == -1) fimValor = json.indexOf('}', inicioValor);
        
        if (inicioValor == -1 || fimValor == -1) return 0.0;
        
        String valorStr = json.substring(inicioValor, fimValor).trim();
        return Double.parseDouble(valorStr);
    }
    
    /**
     * 📊 CLASSE SIMPLES PARA CONFIGURAÇÕES DE TEMPO
     */
    public static class ConfiguracoesTempo {
        public final double duracaoInicial;
        public final double duracaoFinal;
        public final double tempoAceleracao;
        
        public ConfiguracoesTempo(double duracaoInicial, double duracaoFinal, double tempoAceleracao) {
            this.duracaoInicial = duracaoInicial;
            this.duracaoFinal = duracaoFinal;
            this.tempoAceleracao = tempoAceleracao;
        }
        
        public double calcularDuracao(double tempoAtual) {
            if (tempoAtual >= tempoAceleracao) {
                return duracaoFinal;
            }
            
            double progresso = Math.min(1.0, tempoAtual / tempoAceleracao);
            return duracaoInicial - ((duracaoInicial - duracaoFinal) * progresso);
        }
    }
}
