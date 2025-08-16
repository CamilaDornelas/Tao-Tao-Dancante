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

    private static final double LARGURA_TOTAL = 400.0;
    private static final double ALTURA_TOTAL = 25.0;
    private static final double RAIO_ARCO = 20.0;
    private static final double TAMANHO_ICONE = 40.0;
    private static final double ESPACAMENTO_ICONES = LARGURA_TOTAL - 60.0;

    private static final String COR_FUNDO = "#808080"; // GRAY
    private static final String COR_ALTA_VIDA = "#32CD32"; // LIMEGREEN
    private static final String COR_MEDIA_VIDA = "#FFFF00"; // YELLOW
    private static final String COR_BAIXA_VIDA = "#FF0000"; // RED

    private Rectangle fundoBarra;
    private Rectangle preenchimentoBarra;
    private ImageView iconeJogador;
    private ImageView iconeOponente;
    private HBox layoutIcones;

    public PlacarDeVida() {
        fundoBarra = criarFundoBarra();
        preenchimentoBarra = criarPreenchimentoBarraInicial();

        iconeJogador = criarIcone("/assets/persona/bardoBarra.png");
        iconeOponente = criarIcone("/assets/persona/lordBarra.png");

        layoutIcones = new HBox();
        layoutIcones.setSpacing(ESPACAMENTO_ICONES);
        layoutIcones.setAlignment(Pos.CENTER);
        layoutIcones.getChildren().addAll(iconeOponente, iconeJogador);

        this.getChildren().addAll(fundoBarra, preenchimentoBarra, layoutIcones);

        atualizar(0.5, null);
    }

    /**
     * @return Rectangle representando o fundo da barra.
     */
    private Rectangle criarFundoBarra() {
        Rectangle fundo = new Rectangle(LARGURA_TOTAL, ALTURA_TOTAL);
        fundo.setFill(Color.web(COR_FUNDO));
        fundo.setArcWidth(RAIO_ARCO);
        fundo.setArcHeight(RAIO_ARCO);
        return fundo;
    }

    /**
     * @return Rectangle representando a barra de preenchimento.
     */
    private Rectangle criarPreenchimentoBarraInicial() {
        Rectangle preenchimento = new Rectangle(LARGURA_TOTAL / 2, ALTURA_TOTAL);
        preenchimento.setFill(Color.web(COR_MEDIA_VIDA));
        preenchimento.setArcWidth(RAIO_ARCO);
        preenchimento.setArcHeight(RAIO_ARCO);
        StackPane.setAlignment(preenchimento, Pos.CENTER_LEFT);
        return preenchimento;
    }

    /**

     * @param caminho Caminho relativo da imagem dentro do projeto.
     * @return ImageView do ícone.
     */
    private ImageView criarIcone(String caminho) {
        Image img = new Image(Objects.requireNonNull(getClass().getResource(caminho)).toExternalForm());
        ImageView icone = new ImageView(img);
        icone.setFitHeight(TAMANHO_ICONE);
        icone.setFitWidth(TAMANHO_ICONE);
        return icone;
    }

    /**
     * @param porcentagem Valor entre 0.0 e 1.0 indicando a proporção da vida.
     * @param lorde Instância do Lorde, pode ser null.
     */
    public void atualizar(double porcentagem, Lorde lorde) {
        double porcentagemValidada = Math.max(0, Math.min(1, porcentagem));
        preenchimentoBarra.setWidth(LARGURA_TOTAL * porcentagemValidada);

        if (porcentagemValidada > 0.6) {
            preenchimentoBarra.setFill(Color.web(COR_ALTA_VIDA));
            if (lorde != null) lorde.ficarComRaiva();
        } else if (porcentagemValidada < 0.4) {
            preenchimentoBarra.setFill(Color.web(COR_BAIXA_VIDA));
            if (lorde != null) lorde.ficarFeliz();
        } else {
            preenchimentoBarra.setFill(Color.web(COR_MEDIA_VIDA));
            if (lorde != null) lorde.ficarPensativo();
        }
    }
}
