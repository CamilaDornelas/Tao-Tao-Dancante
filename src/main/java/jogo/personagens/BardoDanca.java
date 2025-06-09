package jogo.personagens;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class BardoDanca extends ImageView {

        private final Image png1;
        private final Image png2;
        private final Timeline animation;

        public BardoDanca(double width, double height) {
            this.png1 = new Image(getClass().getResource("/assets/persona/bardoDance1.png").toExternalForm());
            this.png2 = new Image(getClass().getResource("/assets/persona/bardoDance2.png").toExternalForm());

            this.setImage(png1);
            this.setFitWidth(width);
            this.setFitHeight(height);

            animation = new Timeline(
                    new KeyFrame(Duration.millis(400), e -> toggleFrame())
            );
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.play(); // Começa dançando assim que for criado (ISSO DEVE SER MUDADO PRA ELE DANÇAR APENAS QUANDO A MUSICA TOCAR)
        }

        private void toggleFrame() {
            if (this.getImage() == png1) {
                this.setImage(png2);
            } else {
                this.setImage(png1);
            }
        }
}
