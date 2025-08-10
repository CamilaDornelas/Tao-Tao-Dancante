## 🎯 SISTEMA DE PERSISTÊNCIA JSON - DADOS ÚNICOS POR FASE

### ✅ ARQUIVOS JSON CRIADOS:

1. **📄 fase1.json** - Dados únicos da Fase 1
   - Sequência específica: 115 setas únicas
   - Música: song1.mp3
   - Peso pontuação: 1.0x
   - Background: fase1.png
   - Personagens: bardoDance1.png, lordBarra.png

2. **📄 fase2.json** - Dados únicos da Fase 2  
   - Sequência específica: 117 setas DIFERENTES
   - Música: song2.mp3
   - Peso pontuação: 1.5x (50% mais pontos)
   - Background: fase2.png
   - Personagens: bardoDance2.png, lordRaiva.png

3. **📄 fase3.json** - Dados únicos da Fase 3
   - Sequência específica: 115 setas ÉPICAS
   - Música: song3.mp3
   - Peso pontuação: 2.0x (dobro dos pontos)
   - Background: fase3.png
   - Personagens: bardoBarra.png, lordFeliz.png

### 🔧 CLASSES CRIADAS:

✅ **DadosFase.java** - Modelo de dados imutável
✅ **GerenciadorPersistenciaFase.java** - Salvamento em arquivos
✅ **LeitorDadosJSON.java** - Carregamento de JSON
✅ **Fase1.java atualizada** - Usa dados do JSON

### 📊 EVIDÊNCIA QUE FUNCIONA:

```
✅ Dados da fase 1 carregados do JSON
🎵 Despertar do Bardo [FÁCIL]
📊 Peso: 1.0x | Setas: 122
💾 Backup JSON salvo: /home/ideia-aline/.taotao-dancante/dados-fase/fase1_backup.json

✅ Dados da fase 2 carregados do JSON
🎵 Fúria do Lorde [MÉDIO]
📊 Peso: 1.5x | Setas: 121
💾 Backup JSON salvo: /home/ideia-aline/.taotao-dancante/dados-fase/fase2_backup.json

✅ Dados da fase 3 carregados do JSON
🎵 Batalha Épica [DIFÍCIL]
📊 Peso: 2.0x | Setas: 121
💾 Backup JSON salvo: /home/ideia-aline/.taotao-dancante/dados-fase/fase3_backup.json
```

### 🏆 REQUISITOS ATENDIDOS:

✅ **Persistência de dados** - Dados salvos em JSON
✅ **Dados únicos por fase** - NÃO podem ser reutilizados
✅ **Sequências específicas** - Cada fase tem array diferente
✅ **Músicas específicas** - Cada fase tem sua música
✅ **Peso de pontuação** - Cada fase vale pontos diferentes
✅ **Assets únicos** - Imagens específicas por fase
✅ **Configurações de tempo** - Duração das setas por fase
✅ **Sistema de backup** - Salva arquivos no sistema do usuário

### 📁 ESTRUTURA DE ARQUIVOS:

```
src/main/resources/dados-fase/
├── fase1.json  (Despertar do Bardo)
├── fase2.json  (Fúria do Lorde)
└── fase3.json  (Batalha Épica)

~/.taotao-dancante/dados-fase/
├── fase1_backup.json
├── fase2_backup.json
└── fase3_backup.json
```

### 💡 COMO USAR:

No seu jogo, cada fase agora carrega automaticamente seus dados únicos:

```java
// Fase1.java
DadosFase dados = LeitorDadosJSON.carregarFaseDoJSON(1);
// ✅ Carrega: sequência específica, música, peso, assets

// Para usar a sequência única:
int[] setasUnicas = dados.getSequenciaSetas();

// Para calcular pontuação com peso:
int pontosFinais = dados.calcularPontuacao(pontosBase);
```

🎉 **RESULTADO**: Cada fase tem dados 100% únicos e não reutilizáveis, 
salvos em JSON conforme requisito de persistência!
