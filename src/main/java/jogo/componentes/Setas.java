package jogo.componentes;


import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane; // Import AnchorPane
import javafx.util.Duration;
import java.util.List; // Import List para o método miss
import java.util.Objects;




public class Setas extends ImageView {


   public enum ArrowType {
       UP("/assets/setas/cima.png"),
       DOWN("/assets/setas/baixo.png"),
       LEFT("/assets/setas/esquerda.png"),
       RIGHT("/assets/setas/direita.png");


       private final String imagePath;


       ArrowType(String imagePath) {
           this.imagePath = imagePath;
       }


       public String getImagePath() {
           return imagePath;
       }
   }


   private ArrowType type;
   private ParallelTransition riseAnimation;


   public Setas(ArrowType type, double width, double height) {
       this.type = type;
       setFitWidth(width);
       setFitHeight(height);
       setImage(new Image(Objects.requireNonNull(getClass().getResource(type.getImagePath())).toExternalForm()));
       setOpacity(0);
   }


   public ArrowType getType() {
       return type;
   }


   public void setType(ArrowType type) {
       this.type = type;
       setImage(new Image(getClass().getResource(type.getImagePath()).toExternalForm()));
   }


   public ParallelTransition startRiseAnimation(double duration, double distance) {
       FadeTransition fadeIn = new FadeTransition(Duration.millis(duration), this);
       fadeIn.setFromValue(0);
       fadeIn.setToValue(1);


       TranslateTransition rise = new TranslateTransition(Duration.millis(duration), this);
       rise.setByY(-distance);


       riseAnimation = new ParallelTransition(fadeIn, rise);
       riseAnimation.play();
       return riseAnimation;
   }


   /**
    * Faz a seta desaparecer instantaneamente (usado para acertos).
    */
   public void hide() {
       this.setVisible(false);
       if (riseAnimation != null && riseAnimation.getStatus() == javafx.animation.Animation.Status.RUNNING) {
           riseAnimation.stop();
       }
   }


   /**
    * Faz a seta desaparecer com uma animação de fade-out (usado para setas "perdidas").
    * @param parentPane O AnchorPane pai para remover a seta após a animação.
    * @param activeArrows A lista de setas ativas para remover a seta.
    */
   public void miss(AnchorPane parentPane, List<Setas> activeArrows) {
       if (riseAnimation != null && riseAnimation.getStatus() == javafx.animation.Animation.Status.RUNNING) {
           riseAnimation.stop(); // Para a animação de subida se ainda estiver rodando
       }


       FadeTransition fadeOut = new FadeTransition(Duration.millis(300), this); // Fade-out em 300ms
       fadeOut.setFromValue(1);
       fadeOut.setToValue(0);
       fadeOut.setOnFinished(event -> {
           // Verifica se a seta ainda é filha do painel antes de tentar remover
           if (parentPane.getChildren().contains(this)) {
               parentPane.getChildren().remove(this); // Remove do painel
           }
           // Verifica se a seta ainda está na lista antes de tentar remover
           if (activeArrows.contains(this)) {
               activeArrows.remove(this); // Remove da lista de ativas
           }
       });
       fadeOut.play();
   }


   public void show() {
       this.setVisible(true);
       this.setOpacity(1);
   }


   public ParallelTransition getRiseAnimation() {
       return riseAnimation;
   }
}
