package jogo.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import jogo.interfaces.PauseManager;

import javafx.event.ActionEvent;


public class PauseController {

    private PauseManager pauseManager;

    public void setPauseManager(PauseManager pauseManager) {
        this.pauseManager = pauseManager;
    }

    @FXML
    private AnchorPane pausePane;

    @FXML
    private void onResumeClicked() {
        pauseManager.resume();
    }

    @FXML
    private void onBackToMenuClicked(ActionEvent event) {
        pauseManager.backToMenu(event);
    }

    @FXML
    private void onExitClicked() {
        pauseManager.exitGame();
    }
}
