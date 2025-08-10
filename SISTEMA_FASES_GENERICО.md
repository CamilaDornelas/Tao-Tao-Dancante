# 🎮 Sistema Genérico de Fases

## ✨ **Visão Geral**

O projeto agora possui um sistema **completamente genérico** para carregar fases dinamicamente! Você pode facilmente adicionar novas fases sem precisar criar classes específicas.

## 🏗️ **Arquitetura**

### **1. FaseGenerica.java** ⭐
```java
// Carrega qualquer fase baseada no número
FaseGenerica fase = new FaseGenerica(2); // Carrega fase 2
```

**Características:**
- ✅ Carrega dados do JSON automaticamente
- ✅ Aplica volume salvo imediatamente
- ✅ Configura personagens dinamicamente
- ✅ Suporte a fallback se JSON falhar

### **2. FaseController.java** 🎯
```java
// Métodos disponíveis
faseController.carregarFase(stage, 1);     // Genérico
faseController.carregarFase1(stage);       // Específico
faseController.carregarFase2(stage);       // Específico
faseController.carregarFase3(stage);       // Específico
faseController.carregarProximaFase(stage, 2); // Progressão
```

## 🚀 **Como Adicionar Novas Fases**

### **Passo 1: Criar o JSON da fase**
```json
// src/main/resources/dados-fase/fase4.json
{
  "numeroFase": 4,
  "nomeFase": "Nova Fase Épica",
  "dificuldade": "INSANO",
  "pesoPontuacao": 3.0,
  "configuracoesTempo": {
    "duracaoSetasInicial": 2000.0,
    "duracaoSetasFinal": 1000.0,
    "tempoAceleracao": 10000.0
  },
  "assets": {
    "caminhoMusica": "/assets/musica/song4.mp3",
    "imagemBardo": "/assets/persona/bardoEpico.png",
    "imagemLorde": "/assets/persona/lordeFinal.png",
    "imagemBackground": "/assets/imagens/fase4.png"
  },
  "sequenciaSetas": [0, 1, 2, 3, 3, 2, 1, 0...]
}
```

### **Passo 2: Adicionar os assets**
```
src/main/resources/assets/
├── musica/song4.mp3
├── imagens/fase4.png
└── persona/
    ├── bardoEpico.png
    └── lordeFinal.png
```

### **Passo 3: Usar no código**
```java
// Carrega a nova fase automaticamente!
faseController.carregarFase(stage, 4);
```

## 📋 **Métodos Úteis do FaseController**

### **🎯 Carregamento Básico**
```java
// Carrega fase específica
faseController.carregarFase(stage, numeroFase);

// Métodos específicos (compatibilidade)
faseController.carregarFase1(stage);
faseController.carregarFase2(stage);
faseController.carregarFase3(stage);
```

### **🔄 Progressão de Fases**
```java
// Carrega próxima fase automaticamente
faseController.carregarProximaFase(stage, faseAtual);

// Reinicia fase atual
faseController.reiniciarFase(stage, numeroFase);
```

### **📋 Utilitários**
```java
// Lista fases disponíveis
faseController.listarFasesDisponiveis();
```

## 🎮 **Exemplo de Uso Completo**

### **SelecionadorDeFases.java** (exemplo criado)
```java
public class SelecionadorDeFases {
    private final FaseController faseController = new FaseController();

    @FXML
    private void carregarFase1(ActionEvent event) {
        Stage stage = getStage(event);
        faseController.carregarFase1(stage);
    }

    @FXML
    private void carregarFase2(ActionEvent event) {
        Stage stage = getStage(event);
        faseController.carregarFase2(stage);
    }

    // Método genérico para qualquer fase
    public void carregarFasePersonalizada(ActionEvent event, int numeroFase) {
        Stage stage = getStage(event);
        faseController.carregarFase(stage, numeroFase);
    }
}
```

## 🏆 **Vantagens do Novo Sistema**

### **✨ Para Desenvolvedores:**
- 🧹 **Menos código**: Uma classe genérica ao invés de Fase1, Fase2, Fase3...
- 🔧 **Fácil manutenção**: Dados no JSON, lógica no código
- 🚀 **Escalável**: Adicionar novas fases é trivial
- 🎯 **Flexível**: Suporte a qualquer número de fases

### **✨ Para o Jogo:**
- 🎵 **Volume persistente**: Aplicado automaticamente
- 📊 **Dados dinâmicos**: Cada fase com características únicas
- 🖼️ **Assets dinâmicos**: Imagens e música específicas por fase
- ⚡ **Performance**: Sistema otimizado e eficiente

## 🔧 **Compatibilidade**

O sistema mantém **100% de compatibilidade** com o código existente:
- ✅ `carregarFase1()` ainda funciona
- ✅ Todos os controllers existentes funcionam
- ✅ FXML atualizado para suporte dinâmico

## 📈 **Próximos Passos**

1. **Criar JSONs para Fase 2 e 3** se necessário
2. **Adicionar mais assets** (músicas, imagens)
3. **Implementar sistema de progressão** entre fases
4. **Criar tela de seleção de fases** para o usuário
5. **Adicionar sistema de saves** por fase

---

🎉 **Agora você pode facilmente adicionar quantas fases quiser apenas criando arquivos JSON!**
