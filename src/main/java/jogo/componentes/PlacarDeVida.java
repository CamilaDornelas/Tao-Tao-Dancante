package jogo.componentes;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Objects;

import jogo.personagens.Lorde;


public class PlacarDeVida extends StackPane {

    private final double LARGURA_TOTAL = 400;
    private final double ALTURA_TOTAL = 25;

    private Rectangle fundo;
    private Rectangle preenchimento;
    private ImageView iconeJogador;
    private ImageView iconeOponente;
    private HBox layoutIcones;


    public PlacarDeVida() {


        fundo = new Rectangle(LARGURA_TOTAL, ALTURA_TOTAL);
        fundo.setFill(Color.GRAY);
        fundo.setArcWidth(20);
        fundo.setArcHeight(20);


        preenchimento = new Rectangle(LARGURA_TOTAL / 2, ALTURA_TOTAL);
        preenchimento.setFill(Color.LIMEGREEN);
        preenchimento.setArcWidth(20);
        preenchimento.setArcHeight(20);


        StackPane.setAlignment(preenchimento, Pos.CENTER_LEFT);


        iconeJogador = criarIcone("/assets/persona/bardoBarra.png");
        iconeOponente = criarIcone("/assets/persona/lordBarra.png");


        layoutIcones = new HBox();
        layoutIcones.setSpacing(LARGURA_TOTAL - 60);
        layoutIcones.setAlignment(Pos.CENTER);
        layoutIcones.getChildren().addAll(iconeOponente, iconeJogador);


        this.getChildren().addAll(fundo, preenchimento, layoutIcones);


        atualizar(0.5, null);
    }

    private ImageView criarIcone(String caminho) {
        Image img = new Image(Objects.requireNonNull(getClass().getResource(caminho)).toExternalForm());
        ImageView icone = new ImageView(img);
        icone.setFitHeight(40);
        icone.setFitWidth(40);
        return icone;
    }


    public void atualizar(double porcentagem, Lorde lorde) {
        porcentagem = Math.max(0, Math.min(1, porcentagem));
        preenchimento.setWidth(LARGURA_TOTAL * porcentagem);

        if (porcentagem > 0.60) {
            preenchimento.setFill(Color.LIMEGREEN);
            if (lorde != null) {
                lorde.ficarComRaiva(); // Lorde com raiva
            }
        } else if (porcentagem < 0.40) {
            preenchimento.setFill(Color.RED);
            if (lorde != null) {
                lorde.ficarFeliz(); // Lorde com raiva
            }
        } else {
            preenchimento.setFill(Color.YELLOW);
            if (lorde != null) {
                lorde.ficarPensativo(); // Lorde pensativo
            }
        }
    }

}