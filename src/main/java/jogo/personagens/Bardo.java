package jogo.personagens;

import jogo.modelo.enume.TipoPersonagem;
import jogo.excecoes.PersonagemInvalidoException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;


public class Bardo extends PersonagemJogo {

    private static final double DURACAO_KEYFRAME_MILLIS = 400.0;
    private static final double TAXA_ANIMACAO_HABILIDADE = 1.5;

    // Atributos específicos do Bardo
    private int bonusRitmo;
    private boolean inspiracaoAtiva;

    // Animações
    private final Image imagemPadrao;
    private final Image imagemAlternativa;
    private final Timeline animacao;


    public Bardo(double largura, double altura) throws PersonagemInvalidoException {
        super("Bardo", TipoPersonagem.BARDO, largura, altura);

        this.bonusRitmo = 10;
        this.inspiracaoAtiva = false;

        // Carregamento de imagens
        this.imagemPadrao = new Image(getClass().getResource("/assets/persona/bardoDance1.png").toExternalForm());
        this.imagemAlternativa = new Image(getClass().getResource("/assets/persona/bardoDance2.png").toExternalForm());

        this.setImage(imagemPadrao);

        // Configuração da animação
        this.animacao = new Timeline(
                new KeyFrame(Duration.millis(DURACAO_KEYFRAME_MILLIS), e -> alternarFrame())
        );
        this.animacao.setCycleCount(Timeline.INDEFINITE);
        this.animacao.play();
    }


    private void alternarFrame() {
        if (this.getImage() == imagemPadrao) {
            this.setImage(imagemAlternativa);
        } else {
            this.setImage(imagemPadrao);
        }
    }

    /**
     * Retorna a linha do tempo da animação.
     */
    public Timeline getAnimacao() {
        return animacao;
    }

    @Override
    public void executarMovimento() {
        System.out.println(getNome() + " executa um movimento musical ritmado!");

        int pontosBase = getTipo().getBonusPontuacao();
        int pontosComBonus = pontosBase + bonusRitmo;

        if (inspiracaoAtiva) {
            pontosComBonus *= 2;
            inspiracaoAtiva = false;
        }

        adicionarPontos(pontosComBonus);
    }

    @Override
    public void usarHabilidadeEspecial() {
        System.out.println(getNome() + " usa Inspiração Bardica!");
        this.inspiracaoAtiva = true;
        this.bonusRitmo += 5;
        setMultiplicadorPontos(getMultiplicadorPontos() + 0.2);

        animacao.setRate(TAXA_ANIMACAO_HABILIDADE);
    }

    @Override
    public String obterDescricao() {
        return "Bardo " + getNome() + " - Especialista em ritmo e música. " +
                "Bônus de ritmo: " + bonusRitmo +
                (inspiracaoAtiva ? " (Inspiração Ativa!)" : "");
    }
}