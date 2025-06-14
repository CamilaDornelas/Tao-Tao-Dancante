package jogo.personagens;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;


public class BardoDanca extends Personagem {

        private final Image png1;
        private final Image png2;
        private final Timeline animacoes;

        public BardoDanca(double width, double height) {
            super(width, height);
            this.png1 = new Image(getClass().getResource("/assets/persona/bardoDance1.png").toExternalForm());
            this.png2 = new Image(getClass().getResource("/assets/persona/bardoDance2.png").toExternalForm());

            this.setImage(png1);

            animacoes = new Timeline(
                    new KeyFrame(Duration.millis(400), e -> toggleFrame())
            );
            animacoes.setCycleCount(Timeline.INDEFINITE);
            animacoes.play(); // Começa dançando
        }

        private void toggleFrame() {
            if (this.getImage() == png1) {
                this.setImage(png2);
            } else {
                this.setImage(png1);
            }
        }


    public Timeline getAnimacao() {
        return animacoes;
    }
}
