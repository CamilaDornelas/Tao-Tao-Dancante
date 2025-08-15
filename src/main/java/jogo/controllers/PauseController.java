package jogo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import jogo.servicos.GestorDePause;
import jogo.componentes.ControleVolume;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class PauseController implements Initializable {

    private static final double POSICAO_ESQUERDA_CONTROLE_VOLUME = 20.0;
    private static final double POSICAO_INFERIOR_CONTROLE_VOLUME = 20.0;

    private GestorDePause gestorDePause;
    private ControleVolume controleVolume;
    private MediaPlayer reprodutorMidia;

    @FXML
    private AnchorPane telaPause;


    public void setGestorDePause(GestorDePause gestorDePause) {
        this.gestorDePause = gestorDePause;
    }

    /**
     * ✨ NOVO: Define o MediaPlayer para controle de volume
     */
    public void setReprodutorMidia(MediaPlayer reprodutorMidia) {
        this.reprodutorMidia = reprodutorMidia;

        System.out.println("🎵 setReprodutorMidia chamado!");

        if (controleVolume != null) {
            controleVolume.setReprodutorMidia(reprodutorMidia);
            System.out.println("🔗 MediaPlayer conectado ao controle de volume");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("🎵 PauseController initialize() chamado!");
        System.out.println("🎵 telaPause é null? " + (telaPause == null));

        if (telaPause != null) {
            criarEAdicionarControleVolume();
            conectarReprodutorMidiaSeDisponivel();
        }
    }


    private void criarEAdicionarControleVolume() {
        controleVolume = new ControleVolume();

        AnchorPane.setLeftAnchor(controleVolume, POSICAO_ESQUERDA_CONTROLE_VOLUME);
        AnchorPane.setBottomAnchor(controleVolume, POSICAO_INFERIOR_CONTROLE_VOLUME);

        telaPause.getChildren().add(controleVolume);

        System.out.println("🎵 Controle de volume adicionado à tela de pause no initialize()");
    }


    private void conectarReprodutorMidiaSeDisponivel() {
        if (reprodutorMidia != null) {
            controleVolume.setReprodutorMidia(reprodutorMidia);
            System.out.println("🔗 MediaPlayer conectado ao controle de volume no initialize()");
        }
    }

    @FXML
    private void voltarAoJogo() {
        gestorDePause.voltar();
    }

    @FXML
    private void voltarAoMenu(ActionEvent evento) {
        gestorDePause.voltarParaMenu(evento);
    }

    @FXML
    private void sairDoJogo() {
        gestorDePause.sairDoJogo();
    }
}