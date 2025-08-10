package jogo.componentes;


import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.util.List;
import java.util.Objects;
import java.lang.Runnable;


public class Setas extends ImageView {


    public enum TipoSetas {
        UP("/assets/setas/cima.png"),
        DOWN("/assets/setas/baixo.png"),
        LEFT("/assets/setas/esquerda.png"),
        RIGHT("/assets/setas/direita.png");


        private final String imagePath;


        TipoSetas(String imagePath) {
            this.imagePath = imagePath;
        }


        public String getImagePath() {
            return imagePath;
        }
    }


    private TipoSetas type;
    private ParallelTransition riseAnimation;
    private final Runnable missAction;


    public Setas(TipoSetas tipo, double largura, double altura, Runnable missAction) {
        this.type = tipo;
        this.missAction = missAction;
        setFitWidth(largura);
        setFitHeight(altura);
        setImage(new Image(Objects.requireNonNull(getClass().getResource(tipo.getImagePath())).toExternalForm()));
        setOpacity(0);
    }


    public TipoSetas getType() {
        return type;
    }


    public void setType(TipoSetas tipo) {
        this.type = tipo;
        setImage(new Image(getClass().getResource(type.getImagePath()).toExternalForm()));
    }


    public ParallelTransition subirSetas(double duracao, double diatancia) {
        FadeTransition subindoSetas = new FadeTransition(Duration.millis(duracao), this);
        subindoSetas.setFromValue(0);
        subindoSetas.setToValue(1);


        TranslateTransition subir = new TranslateTransition(Duration.millis(duracao), this);
        subir.setByY(-diatancia);


        riseAnimation = new ParallelTransition(subindoSetas, subir);
        riseAnimation.play();
        return riseAnimation;
    }



    public void esconder() {
        this.setVisible(false);
        if (riseAnimation != null && riseAnimation.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            riseAnimation.stop();
        }
    }


    public void errar(AnchorPane parentPane, List<Setas> activeArrows) {
        if (parentPane.getChildren().contains(this)) {
            parentPane.getChildren().remove(this);
        }
        if (activeArrows.contains(this)) {
            activeArrows.remove(this);
        }

        if (missAction != null) {
            missAction.run();
        }
    }



    public void mostar() {
        this.setVisible(true);
        this.setOpacity(1);
    }


    public ParallelTransition getRiseAnimation() {
        return riseAnimation;
    }
     
    public void aplicarEfeitoBrilho() {
    
        this.setStyle("-fx-effect: dropshadow(gaussian, gold, 5, 0.4, 0, 0);");
        
        FadeTransition flash = new FadeTransition();
        flash.setNode(this);
        flash.setDuration(Duration.seconds(0.15));
        flash.setFromValue(1.0);
        flash.setToValue(1.1);
        flash.setCycleCount(1);
        flash.setAutoReverse(false);
        flash.play();
    }
}