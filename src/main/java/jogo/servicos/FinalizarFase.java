package jogo.servicos;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FinalizarFase {

    public static void finalizarFase(Stage stage, boolean vitoria) {
        tocarSom(vitoria);
        carregarTelaFinal(stage, vitoria);
    }

    private static void tocarSom(boolean vitoria) {
        String soundFile = vitoria ? "victory-.mp3" : "losetrumpet.mp3";
        try {
            String soundPath = FinalizarFase.class
                    .getResource("/assets/musica/" + soundFile)
                    .toURI()
                    .toString();
            Media media = new Media(soundPath);
            MediaPlayer player = new MediaPlayer(media);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void carregarTelaFinal(Stage stage, boolean vitoria) {
        try {
            String fxmlPath = vitoria ? "/vitoria/vitoria-view.fxml" : "/perdeu/perdeu-view.fxml";
            FXMLLoader loader = new FXMLLoader(FinalizarFase.class.getResource(fxmlPath));
            Parent root = loader.load();

            root.setOpacity(0.0);
            FadeTransition fade = new FadeTransition(Duration.millis(800), root);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

