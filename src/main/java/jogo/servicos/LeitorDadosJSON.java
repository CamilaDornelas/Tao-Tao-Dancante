package jogo.servicos;

import jogo.modelo.DadosFase;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * ✨ LEITOR DE DADOS JSON DAS FASES
 * Carrega dados únicos de cada fase salvos em arquivos JSON
 * Garante que cada fase tem configurações específicas e não reutilizáveis
 */
public class LeitorDadosJSON {
    
    private static final String PASTA_JSON = "/dados-fase/";
    
    /**
     * 📖 CARREGA DADOS DE UMA FASE ESPECÍFICA DO JSON
     */
    public static DadosFase carregarFaseDoJSON(int numeroFase) {
        String nomeArquivo = String.format("fase%d.json", numeroFase);
        
        try {
            // Lê o arquivo JSON do resources
            InputStream inputStream = LeitorDadosJSON.class.getResourceAsStream(PASTA_JSON + nomeArquivo);
            
            if (inputStream == null) {
                System.err.println("❌ Arquivo JSON não encontrado: " + nomeArquivo);
                throw new FileNotFoundException("Arquivo JSON da fase " + numeroFase + " não encontrado!");
            }
            
            // Lê todo o conteúdo do arquivo
            String conteudoJSON = lerInputStream(inputStream);
            
            // Parse manual do JSON (sem bibliotecas externas)
            DadosJSON dados = parseJSON(conteudoJSON);
            
            System.out.println("✅ Dados da fase " + numeroFase + " carregados do JSON");
            System.out.println("🎵 " + dados.nomeFase + " [" + dados.dificuldade + "]");
            System.out.println("📊 Peso: " + dados.pesoPontuacao + "x | Setas: " + dados.sequenciaSetas.length);
            
            return criarDadosFaseDoJSON(dados);
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar JSON da fase " + numeroFase + ": " + e.getMessage());
            throw new RuntimeException("Falha ao carregar dados da fase " + numeroFase, e);
        }
    }
    
    /**
     * 💾 SALVA DADOS DE UMA FASE EM JSON NO SISTEMA DE ARQUIVOS
     */
    public static boolean salvarFaseEmJSON(DadosFase dados) {
        String pastaUsuario = System.getProperty("user.home") + "/.taotao-dancante/dados-fase/";
        String nomeArquivo = String.format("fase%d_backup.json", dados.getNumeroFase());
        
        try {
            // Cria pasta se não existir
            Files.createDirectories(Paths.get(pastaUsuario));
            
            // Gera JSON a partir dos dados
            String json = gerarJSON(dados);
            
            // Salva no arquivo
            Files.write(Paths.get(pastaUsuario + nomeArquivo), json.getBytes());
            
            System.out.println("💾 Backup JSON salvo: " + pastaUsuario + nomeArquivo);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar JSON: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 🔧 PARSE MANUAL DO JSON (sem bibliotecas externas)
     */
    private static DadosJSON parseJSON(String json) {
        DadosJSON dados = new DadosJSON();
        
        try {
            // Remove espaços e quebras de linha desnecessárias
            json = json.replaceAll("\\s+", " ");
            
            // Extrai campos básicos
            dados.numeroFase = extrairInteiro(json, "numeroFase");
            dados.nomeFase = extrairString(json, "nomeFase");
            dados.dificuldade = extrairString(json, "dificuldade");
            dados.pesoPontuacao = extrairDouble(json, "pesoPontuacao");
            
            // Extrai configurações de tempo
            dados.duracaoSetasInicial = extrairDoubleAninhado(json, "configuracoesTempo", "duracaoSetasInicial");
            dados.duracaoSetasFinal = extrairDoubleAninhado(json, "configuracoesTempo", "duracaoSetasFinal");
            dados.tempoAceleracao = extrairDoubleAninhado(json, "configuracoesTempo", "tempoAceleracao");
            
            // Extrai assets
            dados.caminhoMusica = extrairStringAninhada(json, "assets", "caminhoMusica");
            dados.imagemBardo = extrairStringAninhada(json, "assets", "imagemBardo");
            dados.imagemLorde = extrairStringAninhada(json, "assets", "imagemLorde");
            dados.imagemBackground = extrairStringAninhada(json, "assets", "imagemBackground");
            
            // Extrai sequência de setas
            dados.sequenciaSetas = extrairArrayInteiros(json, "sequenciaSetas");
            
            return dados;
            
        } catch (Exception e) {
            System.err.println("❌ Erro no parse JSON: " + e.getMessage());
            throw new RuntimeException("Falha no parse do JSON", e);
        }
    }
    
    /**
     * 🏗️ CRIA DadosFase a partir dos dados do JSON
     */
    private static DadosFase criarDadosFaseDoJSON(DadosJSON dados) {
        // ✨ AGORA USA OS DADOS REAIS DO JSON!
        return DadosFase.criarDoJSON(
            dados.numeroFase,
            dados.nomeFase,
            dados.dificuldade,
            dados.pesoPontuacao,
            dados.sequenciaSetas,
            dados.caminhoMusica,
            dados.imagemBardo,
            dados.imagemLorde,
            dados.imagemBackground,
            dados.duracaoSetasInicial,
            dados.duracaoSetasFinal,
            dados.tempoAceleracao
        );
    }
    
    /**
     * 📝 GERA JSON a partir de DadosFase
     */
    private static String gerarJSON(DadosFase dados) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"numeroFase\": ").append(dados.getNumeroFase()).append(",\n");
        json.append("  \"nomeFase\": \"").append(dados.getNomeFase()).append("\",\n");
        json.append("  \"dificuldade\": \"").append(dados.getDificuldade()).append("\",\n");
        json.append("  \"pesoPontuacao\": ").append(dados.getPesoPontuacao()).append(",\n");
        
        json.append("  \"configuracoesTempo\": {\n");
        json.append("    \"duracaoSetasInicial\": ").append(dados.getDuracaoSetasInicial()).append(",\n");
        json.append("    \"duracaoSetasFinal\": ").append(dados.getDuracaoSetasFinal()).append(",\n");
        json.append("    \"tempoAceleracao\": ").append(dados.getTempoAceleracao()).append("\n");
        json.append("  },\n");
        
        json.append("  \"assets\": {\n");
        json.append("    \"caminhoMusica\": \"").append(dados.getCaminhoMusica()).append("\",\n");
        json.append("    \"imagemBardo\": \"").append(dados.getImagemBardo()).append("\",\n");
        json.append("    \"imagemLorde\": \"").append(dados.getImagemLorde()).append("\",\n");
        json.append("    \"imagemBackground\": \"").append(dados.getImagemBackground()).append("\"\n");
        json.append("  },\n");
        
        json.append("  \"sequenciaSetas\": ").append(Arrays.toString(dados.getSequenciaSetas())).append(",\n");
        json.append("  \"backup\": true\n");
        json.append("}");
        
        return json.toString();
    }
    
    // ====== MÉTODOS AUXILIARES PARA PARSE JSON ======
    
    private static String lerInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultado = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                resultado.append(linha).append("\n");
            }
        }
        return resultado.toString();
    }
    
    private static int extrairInteiro(String json, String campo) {
        String pattern = "\"" + campo + "\":\\s*(\\d+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }
    
    private static double extrairDouble(String json, String campo) {
        String pattern = "\"" + campo + "\":\\s*([\\d.]+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        return m.find() ? Double.parseDouble(m.group(1)) : 0.0;
    }
    
    private static String extrairString(String json, String campo) {
        String pattern = "\"" + campo + "\":\\s*\"([^\"]+)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : "";
    }
    
    private static double extrairDoubleAninhado(String json, String objeto, String campo) {
        String pattern = "\"" + objeto + "\":\\s*\\{[^}]*\"" + campo + "\":\\s*([\\d.]+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        return m.find() ? Double.parseDouble(m.group(1)) : 0.0;
    }
    
    private static String extrairStringAninhada(String json, String objeto, String campo) {
        String pattern = "\"" + objeto + "\":\\s*\\{[^}]*\"" + campo + "\":\\s*\"([^\"]+)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : "";
    }
    
    private static int[] extrairArrayInteiros(String json, String campo) {
        String pattern = "\"" + campo + "\":\\s*\\[([^\\]]+)\\]";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        
        if (m.find()) {
            String arrayStr = m.group(1);
            String[] elementos = arrayStr.split(",");
            int[] resultado = new int[elementos.length];
            
            for (int i = 0; i < elementos.length; i++) {
                resultado[i] = Integer.parseInt(elementos[i].trim());
            }
            
            return resultado;
        }
        
        return new int[0];
    }
    
    // ====== CLASSE AUXILIAR PARA DADOS DO JSON ======
    
    private static class DadosJSON {
        int numeroFase;
        String nomeFase;
        String dificuldade;
        double pesoPontuacao;
        double duracaoSetasInicial;
        double duracaoSetasFinal;
        double tempoAceleracao;
        String caminhoMusica;
        String imagemBardo;
        String imagemLorde;
        String imagemBackground;
        int[] sequenciaSetas;
    }
    
    /**
     * 🧪 TESTE DO SISTEMA
     */
    public static void main(String[] args) {
        System.out.println("🧪 TESTANDO LEITOR DE JSON...\n");
        
        for (int fase = 1; fase <= 3; fase++) {
            System.out.println("--- TESTANDO FASE " + fase + " ---");
            DadosFase dados = carregarFaseDoJSON(fase);
            System.out.println("✅ " + dados.toString());
            
            // Testa salvamento
            salvarFaseEmJSON(dados);
            System.out.println();
        }
        
        System.out.println("✅ TESTE CONCLUÍDO!");
    }
}
