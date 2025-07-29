package jogo.personagens;

import javafx.scene.image.Image;
import java.util.Objects;

public class Lorde extends Personagem {



    private final Image imgPensador;
    private final Image imgRaiva;
    private final Image imgFeliz;


    public Lorde(double width, double height) {
        super(width, height);



        imgPensador = new Image(Objects.requireNonNull(getClass().getResource("/assets/persona/lordThinker.png")).toExternalForm());
        imgRaiva = new Image(Objects.requireNonNull(getClass().getResource("/assets/persona/lordRaiva.png")).toExternalForm());
        imgFeliz = new Image(Objects.requireNonNull(getClass().getResource("/assets/persona/lordFeliz.png")).toExternalForm());



        this.setImage(imgPensador);
    }


    public void ficarPensativo() {
        this.setImage(imgPensador);
    }


    public void ficarComRaiva() {
        this.setImage(imgRaiva);
    }


    public void ficarFeliz() {
        this.setImage(imgFeliz);
    }
}