package jogo.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jogo.fases.FaseGenerica;


 //CONTROLADOR DE FASES GENÉRICO

public class FaseController {

    private static final int NUMERO_MAXIMO_FASES = 3;
    private static final String TITULO_JANELA_BASE = "Tao Tao Dancante - Fase ";
    private static final String CAMINHO_VIEW_FASES = "/fases/fase-view.fxml";


     //Carrega qualquer fase pelo número

    public void carregarFase(Stage stage, int numeroFase) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(CAMINHO_VIEW_FASES));
            loader.setController(new FaseGenerica(numeroFase));

            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setTitle(TITULO_JANELA_BASE + numeroFase);
            stage.setScene(scene);
            stage.show();


        } catch (Exception e) {
            System.err.println("Erro ao carregar fase " + numeroFase + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    //METODO ESPECÍFICO: Carrega Fase 1 (mantido para compatibilidade)

    public void carregarFase1(Stage stage) {
        carregarFase(stage, 1);
    }


     //METODO ESPECÍFICO: Carrega Fase 2

    public void carregarFase2(Stage stage) {
        carregarFase(stage, 2);
    }


     //METODO ESPECÍFICO: Carrega Fase 3

    public void carregarFase3(Stage stage) {
        carregarFase(stage, 3);
    }


    //METODO UTILITÁRIO: Carrega próxima fase

    public void carregarProximaFase(Stage stage, int faseAtual) {
        int proximaFase = faseAtual + 1;

        if (proximaFase <= NUMERO_MAXIMO_FASES) {
            carregarFase(stage, proximaFase);
        } else {
            System.out.println("🏆 Todas as fases foram completadas!");
        }
    }

    // lista fases disponíveis (PRO FUTURO)
    public void listarFasesDisponiveis() {

    }
}