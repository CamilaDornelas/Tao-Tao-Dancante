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


    private List<Setas> setasAtivas = new ArrayList<>();
    private Timeline timelineSpawSetas;
    private Random random = new Random();


    private double startX = 340;
    private double spacing = 130;
    private double arrowWidth = 100;
    private double arrowHeight = 100;


    private double initialArrowY = 900;
    private double targetArrowY = 40;
    private double subidaDuracao = 2000;
    private double subidDistancia = initialArrowY - targetArrowY;


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
                arrowHeight + 50
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




        GestorDePause = new GestorDePause(telaFase1, bardo.getAnimacao(), audio, setasAtivas, this::startArrowSpawning);


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
        if (timelineSpawSetas != null) {
            timelineSpawSetas.stop(); // Para a timeline anterior, se houver
        }
        timelineSpawSetas = new Timeline(new KeyFrame(Duration.millis(800), event -> {
            spawnRandomArrow(); // Chama o método para gerar uma nova seta
        }));
        timelineSpawSetas.setCycleCount(Timeline.INDEFINITE); // Define para repetir indefinidamente
        timelineSpawSetas.play(); // Inicia a nova timeline
    }

    private void stopArrowSpawning() {
        if (timelineSpawSetas != null) {
            timelineSpawSetas.stop();
        }
    }


    private void spawnRandomArrow() {
        Setas.TipoSetas[] types = Setas.TipoSetas.values();
        Setas.TipoSetas randomType = types[random.nextInt(types.length)];


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
        setasAtivas.add(newArrow);


        newArrow.subirSetas(subidaDuracao, subidDistancia).setOnFinished(event -> {
            // Se a seta ainda estiver visível ao final da animação, significa que não foi clicada (errar)
            if (newArrow.isVisible()) {
                newArrow.errar(telaFase1, setasAtivas); // Chama o método errar para lidar com o desaparecimento
            }
        });
    }


    private void handleKeyPress(KeyCode code) {
        Setas.TipoSetas pressedType = null;
        switch (code) {
            case LEFT:
                pressedType = Setas.TipoSetas.LEFT;
                break;
            case DOWN:
                pressedType = Setas.TipoSetas.DOWN;
                break;
            case UP:
                pressedType = Setas.TipoSetas.UP;
                break;
            case RIGHT:
                pressedType = Setas.TipoSetas.RIGHT;
                break;
            default:
                return;
        }


        Iterator<Setas> iterator = setasAtivas.iterator();
        while (iterator.hasNext()) {
            Setas arrow = iterator.next();
            if (arrow.getType() == pressedType && arrow.isVisible()) {
                double currentArrowY = arrow.getLayoutY() + arrow.getTranslateY();
                double hitZoneTop = hitZone.getLayoutY();
                double hitZoneBottom = hitZone.getLayoutY() + hitZone.getHeight();


                if (currentArrowY + arrow.getFitHeight() >= hitZoneTop && currentArrowY <= hitZoneBottom) {
                    System.out.println("Acerto: Seta " + pressedType + " acertada!");
                    arrow.esconder(); // Faz a seta desaparecer instantaneamente (feedback de acerto)
                    telaFase1.getChildren().remove(arrow); // Remove do painel
                    iterator.remove(); // Remove da lista de ativas
                    return;
                }
            }
        }
        System.out.println("Erro: Nenhuma seta " + pressedType + " na zona de acerto para acertar.");
    }
}


