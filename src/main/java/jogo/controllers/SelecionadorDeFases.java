package jogo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;


public class SelecionadorDeFases {

    private final FaseController faseController = new FaseController();

    /**
     * 🎵 Carrega Fase 1 - Despertar do Bardo
     */
    @FXML
    private void carregarFase1(ActionEvent evento) {
        Stage palco = getPalco(evento);
        faseController.carregarFase(palco, 1);

        System.out.println("🎵 Iniciando Fase 1: Despertar do Bardo");
    }

    /**
     * 🔥 Carrega Fase 2 - Fúria do Lorde
     */
    @FXML
    private void carregarFase2(ActionEvent evento) {
        Stage palco = getPalco(evento);
        faseController.carregarFase(palco, 2);

        System.out.println("🔥 Iniciando Fase 2: Fúria do Lorde");
    }

    /**
     * ⚡ Carrega Fase 3 - Batalha Épica
     */
    @FXML
    private void carregarFase3(ActionEvent evento) {
        Stage palco = getPalco(evento);
        faseController.carregarFase(palco, 3);

        System.out.println("⚡ Iniciando Fase 3: Batalha Épica");
    }


    public void carregarFasePersonalizada(ActionEvent evento, int numeroFase) {
        Stage palco = getPalco(evento);
        faseController.carregarFase(palco, numeroFase);

        System.out.println("🎯 Iniciando Fase " + numeroFase + " (personalizada)");
    }


    public void proximaFase(ActionEvent evento, int faseAtual) {
        Stage palco = getPalco(evento);
        faseController.carregarProximaFase(palco, faseAtual);
    }


    @FXML
    private void listarFases(ActionEvent evento) {
        faseController.listarFasesDisponiveis();
    }


    private Stage getPalco(ActionEvent evento) {
        return (Stage) ((Node) evento.getSource()).getScene().getWindow();
    }
}