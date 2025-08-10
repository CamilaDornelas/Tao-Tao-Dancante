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

    private GestorDePause gestorDePause;
    private ControleVolume controleVolume; // ✨ NOVO: Controle de volume
    private MediaPlayer mediaPlayer; // ✨ NOVO: Referência ao áudio

    public void setGestorDePause(GestorDePause gestorDePause) {
        this.gestorDePause = gestorDePause;
    }

    /**
     * ✨ NOVO: Define o MediaPlayer para controle de volume
     */
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        
        System.out.println("🎵 setMediaPlayer chamado!");
        
        // Se o controle já foi criado no initialize(), conectar agora
        if (controleVolume != null) {
            controleVolume.setMediaPlayer(mediaPlayer);
            System.out.println("🔗 MediaPlayer conectado ao controle de volume");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("🎵 PauseController initialize() chamado!");
        System.out.println("🎵 telaPause é null? " + (telaPause == null));
        
        if (telaPause != null) {
            // Criar controle de volume
            controleVolume = new ControleVolume();
            
            // Posicionar no canto inferior esquerdo da tela de pause
            AnchorPane.setLeftAnchor(controleVolume, 20.0);
            AnchorPane.setBottomAnchor(controleVolume, 20.0);
            
            // Adicionar à tela de pause
            telaPause.getChildren().add(controleVolume);
            
            System.out.println("🎵 Controle de volume adicionado à tela de pause no initialize()");
            
            // Se já temos MediaPlayer, conectar agora
            if (mediaPlayer != null) {
                controleVolume.setMediaPlayer(mediaPlayer);
                System.out.println("🔗 MediaPlayer conectado ao controle de volume no initialize()");
            }
        }
    }

    @FXML
    private AnchorPane telaPause; // ✨ CORRECTED: Nome deve coincidir com fx:id no FXML

    @FXML
    private void voltarAoJogo() {
        gestorDePause.voltar();
    }

    @FXML
    private void voltarAoMenu(ActionEvent event) {
        gestorDePause.voltarParaMenu(event);
    }

    @FXML
    private void sairDoJogo() {
        gestorDePause.sairDoJogo();
    }
}
