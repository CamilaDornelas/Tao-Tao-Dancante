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

        // 1. Fundo cinza da barra
        fundo = new Rectangle(LARGURA_TOTAL, ALTURA_TOTAL);
        fundo.setFill(Color.GRAY);
        fundo.setArcWidth(20);
        fundo.setArcHeight(20);

        // 2. Preenchimento colorido (começa em 50%)
        preenchimento = new Rectangle(LARGURA_TOTAL / 2, ALTURA_TOTAL);
        preenchimento.setFill(Color.LIMEGREEN); // Cor para o jogador
        preenchimento.setArcWidth(20);
        preenchimento.setArcHeight(20);

        // Alinha o preenchimento à esquerda dentro do StackPane
        StackPane.setAlignment(preenchimento, Pos.CENTER_LEFT);

        // 3. Ícones
        iconeJogador = criarIcone("/assets/persona/bardoBarra.png");
        iconeOponente = criarIcone("/assets/persona/lordBarra.png");

        // 4. Layout para os ícones
        layoutIcones = new HBox();
        layoutIcones.setSpacing(LARGURA_TOTAL - 60); // Espaçamento para colocar os ícones nas pontas
        layoutIcones.setAlignment(Pos.CENTER);
        layoutIcones.getChildren().addAll(iconeOponente, iconeJogador);

        // Adiciona tudo ao StackPane
        this.getChildren().addAll(fundo, preenchimento, layoutIcones);

        // Atualiza a barra para o estado inicial (50%)
        atualizar(0.5, null);
    }

    private ImageView criarIcone(String caminho) {
        Image img = new Image(Objects.requireNonNull(getClass().getResource(caminho)).toExternalForm());
        ImageView icone = new ImageView(img);
        icone.setFitHeight(40);
        icone.setFitWidth(40);
        return icone;
    }

    /**
     * Atualiza a barra de vida com base em uma porcentagem (0.0 a 1.0).
     * 0.0 = Oponente venceu, 0.5 = Neutro, 1.0 = Jogador venceu.
     * @param porcentagem Pontuação atual do jogador.
     */
//    public void atualizar(double porcentagem) {
//        // Garante que a porcentagem fique entre 0 e 1
//        porcentagem = Math.max(0, Math.min(1, porcentagem));
//
//        double novaLargura = LARGURA_TOTAL * porcentagem;
//        preenchimento.setWidth(novaLargura);
//
//        if (porcentagem > 0.60) {
//            preenchimento.setFill(Color.LIMEGREEN);
//
//        } else if (porcentagem < 0.40) {
//            preenchimento.setFill(Color.RED);
//
//        } else {
//            preenchimento.setFill(Color.YELLOW);
//        }
//    }
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