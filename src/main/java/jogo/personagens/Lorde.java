package jogo.personagens;

import javafx.scene.image.Image;
import java.util.Objects;

public class Lorde extends Personagem {

    // Guarda as imagens de expressão para não precisar recarregar
    private final Image imgPensador;
    private final Image imgRaiva;
    private final Image imgFeliz; // Bônus: adicionei uma imagem feliz!

    public Lorde(double width, double height) {
        super(width, height);

        // Carrega as imagens de expressão uma única vez
        imgPensador = new Image(Objects.requireNonNull(getClass().getResource("/assets/persona/lordThinker.png")).toExternalForm());
        imgRaiva = new Image(Objects.requireNonNull(getClass().getResource("/assets/persona/lordRaiva.png")).toExternalForm());
        imgFeliz = new Image(Objects.requireNonNull(getClass().getResource("/assets/persona/lordFeliz.png")).toExternalForm());


        // Define a imagem inicial diretamente no ImageView (que é a própria classe)
        this.setImage(imgPensador);
    }

    /**
     * Altera a imagem do Lorde para a expressão pensativa.
     */
    public void ficarPensativo() {
        this.setImage(imgPensador);
    }

    /**
     * Altera a imagem do Lorde para a expressão de raiva.
     */
    public void ficarComRaiva() {
        this.setImage(imgRaiva);
    }

    /**
     * Altera a imagem do Lorde para a expressão feliz.
     */
    public void ficarFeliz() {
        this.setImage(imgFeliz);
    }
}