package jogo.interfaces;

import javafx.event.ActionEvent;

public interface Pause {
    void pause();
    void resume();
    boolean isPaused();
    void backToMenu(ActionEvent event);
    void exitGame();
}

