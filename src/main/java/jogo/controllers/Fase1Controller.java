package jogo.controllers;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import jogo.interfaces.Pause;
import jogo.personagens.BardoDanca;
import jogo.personagens.Lorde;
import jogo.interfaces.GestorDePause;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import jogo.componentes.Setas;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;




public class Fase1Controller {


    private Pause GestorDePause;


    @FXML
    private AnchorPane telaFase1;


    private List<Setas> activeArrows = new ArrayList<>();
    private Timeline arrowSpawnTimeline;
    private Random random = new Random();


    private double startX = 340;
    private double spacing = 120;
    private double arrowWidth = 100;
    private double arrowHeight = 100;


    private double initialArrowY = 900;
    private double targetArrowY = 40;
    private double riseDuration = 2000;
    private double riseDistance = initialArrowY - targetArrowY;


    private Rectangle hitZone;




    @FXML
    private void initialize() {


        BardoDanca bardo = new BardoDanca(282, 415);
        bardo.setLayoutX(890);
        bardo.setLayoutY(335);
        telaFase1.getChildren().add(bardo);


        Lorde lorde = new Lorde(210, 380);
        lorde.setLayoutX(120);
        lorde.setLayoutY(370);
        telaFase1.getChildren().add(lorde);


        hitZone = new Rectangle(
                startX,
                targetArrowY,
                (3 * spacing) + arrowWidth,
                arrowHeight
        );
        hitZone.setFill(Color.TRANSPARENT);
        hitZone.setStroke(Color.WHITE);
        hitZone.setStrokeWidth(3);
        telaFase1.getChildren().add(hitZone);




        String musica = getClass().getResource("/assets/musica/song1.mp3").toExternalForm();
        Media midia = new Media(musica);
        MediaPlayer audio = new MediaPlayer(midia);




        PauseTransition initialDelay = new PauseTransition(Duration.seconds(3));
        initialDelay.setOnFinished(event -> {
            audio.play();
            startArrowSpawning();
        });
        initialDelay.play();




        GestorDePause = new GestorDePause(telaFase1, bardo.getAnimacao(), audio);


        telaFase1.setFocusTraversable(true);
        Platform.runLater(() -> telaFase1.requestFocus());


        telaFase1.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (!GestorDePause.estaPausado()) {
                    GestorDePause.pause();
                    stopArrowSpawning();
                } else {
                    GestorDePause.voltar();
                    startArrowSpawning();
                }
            } else if (!GestorDePause.estaPausado()) {
                handleKeyPress(event.getCode());
            }
        });


    }


    private void startArrowSpawning() {
        if (arrowSpawnTimeline != null) {
            arrowSpawnTimeline.stop();
        }
        arrowSpawnTimeline = new Timeline(new KeyFrame(Duration.millis(800), event -> {
            spawnRandomArrow();
        }));
        arrowSpawnTimeline.setCycleCount(Timeline.INDEFINITE);
        arrowSpawnTimeline.play();
    }


    private void stopArrowSpawning() {
        if (arrowSpawnTimeline != null) {
            arrowSpawnTimeline.stop();
        }
    }


    private void spawnRandomArrow() {
        Setas.ArrowType[] types = Setas.ArrowType.values();
        Setas.ArrowType randomType = types[random.nextInt(types.length)];


        Setas newArrow = new Setas(randomType, arrowWidth, arrowHeight);


        double arrowX = startX;
        switch (randomType) {
            case LEFT:
                arrowX = startX;
                break;
            case DOWN:
                arrowX = startX + (2 * spacing);
                break;
            case UP:
                arrowX = startX + spacing;
                break;
            case RIGHT:
                arrowX = startX + (3 * spacing);
                break;
        }


        newArrow.setLayoutX(arrowX);
        newArrow.setLayoutY(initialArrowY);
        telaFase1.getChildren().add(newArrow);
        activeArrows.add(newArrow);


        newArrow.startRiseAnimation(riseDuration, riseDistance).setOnFinished(event -> {
            // Se a seta ainda estiver visível ao final da animação, significa que não foi clicada (miss)
            if (newArrow.isVisible()) {
                newArrow.miss(telaFase1, activeArrows); // Chama o método miss para lidar com o desaparecimento
            }
        });
    }


    private void handleKeyPress(KeyCode code) {
        Setas.ArrowType pressedType = null;
        switch (code) {
            case LEFT:
                pressedType = Setas.ArrowType.LEFT;
                break;
            case DOWN:
                pressedType = Setas.ArrowType.DOWN;
                break;
            case UP:
                pressedType = Setas.ArrowType.UP;
                break;
            case RIGHT:
                pressedType = Setas.ArrowType.RIGHT;
                break;
            default:
                return;
        }


        Iterator<Setas> iterator = activeArrows.iterator();
        while (iterator.hasNext()) {
            Setas arrow = iterator.next();
            if (arrow.getType() == pressedType && arrow.isVisible()) {
                double currentArrowY = arrow.getLayoutY() + arrow.getTranslateY();
                double hitZoneTop = hitZone.getLayoutY();
                double hitZoneBottom = hitZone.getLayoutY() + hitZone.getHeight();


                if (currentArrowY + arrow.getFitHeight() >= hitZoneTop && currentArrowY <= hitZoneBottom) {
                    System.out.println("Acerto: Seta " + pressedType + " acertada!");
                    arrow.hide(); // Faz a seta desaparecer instantaneamente (feedback de acerto)
                    telaFase1.getChildren().remove(arrow); // Remove do painel
                    iterator.remove(); // Remove da lista de ativas
                    return;
                }
            }
        }
        System.out.println("Erro: Nenhuma seta " + pressedType + " na zona de acerto para acertar.");
    }
}


