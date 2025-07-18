package jogo.componentes;


import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane; // Import AnchorPane
import javafx.util.Duration;
import java.util.List; // Import List para o método errar
import java.util.Objects;




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


   public Setas(TipoSetas tipo, double largura, double altura) {
       this.type = tipo;
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


   /**
    * Faz a seta desaparecer instantaneamente (usado para acertos).
    */
   public void esconder() {
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
   public void errar(AnchorPane parentPane, List<Setas> activeArrows) {
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


   public void mostar() {
       this.setVisible(true);
       this.setOpacity(1);
   }


   public ParallelTransition getRiseAnimation() {
       return riseAnimation;
   }
}
