package jogo.personagens;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Personagem extends ImageView {

        public Personagem(double width, double height) {
            this.setFitWidth(width);
            this.setFitHeight(height);
        }

        protected void setarImagemInicial(String path) {
            Image imagem = new Image(getClass().getResource(path).toExternalForm());
            this.setImage(imagem);
        }
}
