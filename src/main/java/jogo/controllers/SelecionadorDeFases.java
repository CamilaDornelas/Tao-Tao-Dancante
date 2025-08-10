package jogo.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * ✨ SELECIONADOR DE FASES
 * Exemplo de como carregar diferentes fases dinamicamente
 */
public class SelecionadorDeFases {

    private final FaseController faseController = new FaseController();

    /**
     * 🎵 Carrega Fase 1 - Despertar do Bardo
     */
    @FXML
    private void carregarFase1(ActionEvent event) {
        Stage stage = getStage(event);
        faseController.carregarFase1(stage);
        
        System.out.println("🎵 Iniciando Fase 1: Despertar do Bardo");
    }

    /**
     * 🔥 Carrega Fase 2 - Fúria do Lorde
     */
    @FXML
    private void carregarFase2(ActionEvent event) {
        Stage stage = getStage(event);
        faseController.carregarFase2(stage);
        
        System.out.println("🔥 Iniciando Fase 2: Fúria do Lorde");
    }

    /**
     * ⚡ Carrega Fase 3 - Batalha Épica
     */
    @FXML
    private void carregarFase3(ActionEvent event) {
        Stage stage = getStage(event);
        faseController.carregarFase3(stage);
        
        System.out.println("⚡ Iniciando Fase 3: Batalha Épica");
    }

    /**
     * 🎯 Carrega fase personalizada por número
     */
    public void carregarFasePersonalizada(ActionEvent event, int numeroFase) {
        Stage stage = getStage(event);
        faseController.carregarFase(stage, numeroFase);
        
        System.out.println("🎯 Iniciando Fase " + numeroFase + " (personalizada)");
    }

    /**
     * 🔄 Exemplo de progressão automática de fases
     */
    public void proximaFase(ActionEvent event, int faseAtual) {
        Stage stage = getStage(event);
        faseController.carregarProximaFase(stage, faseAtual);
    }

    /**
     * 📋 Lista fases disponíveis
     */
    @FXML
    private void listarFases(ActionEvent event) {
        faseController.listarFasesDisponiveis();
    }

    /**
     * Método utilitário para obter o Stage do evento
     */
    private Stage getStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
