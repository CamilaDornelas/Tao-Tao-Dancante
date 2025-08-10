package jogo.fases;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import jogo.componentes.PlacarDeVida;
import jogo.personagens.Bardo;
import jogo.personagens.Lorde;
import jogo.servicos.GerenciadorSetas;
import jogo.servicos.GestorDePause;
import jogo.servicos.FinalizarFase;
import javafx.stage.Stage;

public abstract class FaseBase {

    // Constantes para as dimensões e configurações da tela
    private static final double ALTURA_IMAGEM_ERRO = 100.0;
    private static final double LARGURA_IMAGEM_ERRO = 100.0;
    private static final Duration DURACAO_IMAGEM_ERRO = Duration.millis(500);

    // Dimensões e posicionamento da zona de acerto
    private static final double LARGURA_ZONA_ACERTO = (3 * 165) + 190;
    private static final double ALTURA_ZONA_ACERTO = 130;
    private static final double POSICAO_X_ZONA_ACERTO = 300;
    private static final double POSICAO_Y_ZONA_ACERTO = 15;

    // Dimensões e posicionamento do placar de vida
    private static final double POSICAO_X_PLACAR = 400;
    private static final double POSICAO_Y_PLACAR = 700;

    @FXML protected AnchorPane telaFase;
    @FXML protected ImageView background;

    protected MediaPlayer reprodutorMidia;
    protected Rectangle zonaAcerto;
    protected PlacarDeVida placarDeVida;
    protected Lorde lorde;
    protected Bardo bardo;
    protected GerenciadorSetas gerenciadorSetas;
    protected GestorDePause gestorDePause;

    protected boolean jogoFinalizado = false;
    protected double pontuacao = 0.5;

    protected final double GANHO_POR_ACERTO = 0.03;
    protected final double PENALIDADE_POR_ERRO = 0.06;

    private ImageView imagemErro;
    private ImageView imagemMiss;

    @FXML
    protected void initialize() {
        inicializarComponentesDaFase();
        configurarTeclado();
        iniciarFase();
    }

    /**
     * Inicializa todos os componentes visuais e de lógica da fase.
     */
    protected void inicializarComponentesDaFase() {
        inicializarBackground();
        inicializarPersonagens();
        inicializarZonaAcerto();
        inicializarPlacar();
        inicializarMusica();
        inicializarGerenciadorSetas();
    }

    protected abstract void inicializarBackground();
    protected abstract void inicializarPersonagens();
    protected abstract void inicializarMusica();
    protected abstract void iniciarFase();

    protected void definirBackground(String caminhoRelativo) {
        Image imagem = new Image(getClass().getResourceAsStream(caminhoRelativo));
        background.setImage(imagem);
    }

    protected void inicializarZonaAcerto() {
        zonaAcerto = new Rectangle(POSICAO_X_ZONA_ACERTO, POSICAO_Y_ZONA_ACERTO, LARGURA_ZONA_ACERTO, ALTURA_ZONA_ACERTO);
        zonaAcerto.setFill(Color.TRANSPARENT);
        zonaAcerto.setStroke(Color.TRANSPARENT);
        zonaAcerto.setStrokeWidth(3);
        telaFase.getChildren().add(zonaAcerto);
    }

    protected void inicializarPlacar() {
        placarDeVida = new PlacarDeVida();
        placarDeVida.setLayoutX(POSICAO_X_PLACAR);
        placarDeVida.setLayoutY(POSICAO_Y_PLACAR);
        telaFase.getChildren().add(placarDeVida);
    }

    protected void inicializarGerenciadorSetas() {
        gerenciadorSetas = new GerenciadorSetas(telaFase, zonaAcerto, reprodutorMidia, placarDeVida, this::verificarResultadoFinal);
        gerenciadorSetas.setAcaoErro(this::exibirImagemErro);
        gerenciadorSetas.setAcaoMiss(this::exibirImagemMiss);
        gestorDePause = GestorDePause.getInstance(
                telaFase,
                bardo.getAnimacao(),
                reprodutorMidia,
                gerenciadorSetas.getSetasAtivas(),
                gerenciadorSetas::gerarSeta,
                gerenciadorSetas
        );
    }

    protected void configurarTeclado() {
        telaFase.setFocusTraversable(true);
        Platform.runLater(telaFase::requestFocus);

        telaFase.setOnKeyPressed(evento -> {
            if (jogoFinalizado) {
                return;
            }

            if (evento.getCode() == KeyCode.ESCAPE) {
                if (gestorDePause.estaPausado()) {
                    gestorDePause.voltar();
                } else {
                    gestorDePause.pause();
                }
            } else {
                gerenciadorSetas.processarTecla(evento.getCode());
            }
        });
    }

    protected void atualizarPontuacao(boolean acerto) {
        if (jogoFinalizado) return;

        double deltaPontuacao = acerto ? GANHO_POR_ACERTO : -PENALIDADE_POR_ERRO;
        pontuacao += deltaPontuacao;
        pontuacao = Math.max(0.0, Math.min(1.0, pontuacao));

        placarDeVida.atualizar(pontuacao, lorde);

        if (pontuacao <= 0) {
            finalizarFase(false);
        }
    }

    protected void verificarResultadoFinal() {
        boolean venceu = pontuacao > 0.5;
        finalizarFase(venceu);
    }

    protected void finalizarFase(boolean vitoria) {
        if (jogoFinalizado) return;
        jogoFinalizado = true;

        gerenciadorSetas.setJogoTerminou(true);
        if (reprodutorMidia != null) {
            reprodutorMidia.stop();
        }
        gerenciadorSetas.pararSetas();

        Stage palco = (Stage) telaFase.getScene().getWindow();
        if (palco != null) {
            FinalizarFase.finalizarFase(palco, vitoria);
        }
    }

    protected void exibirImagemErro() {
        if (imagemErro != null) return;

        try {
            imagemErro = criarImagemFeedback("/assets/erro/erro.png");
            telaFase.getChildren().add(imagemErro);

            configurarTransicaoDesaparecimento(imagemErro);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem de erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void exibirImagemMiss() {
        if (imagemMiss != null) return;

        try {
            imagemMiss = criarImagemFeedback("/assets/erro/miss.png");
            telaFase.getChildren().add(imagemMiss);

            configurarTransicaoDesaparecimento(imagemMiss);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem de miss: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cria uma ImageView centralizada na tela com base em um caminho de imagem.
     * @param caminho O caminho do recurso da imagem.
     * @return Uma ImageView configurada.
     */
    private ImageView criarImagemFeedback(String caminho) {
        Image imagem = new Image(getClass().getResourceAsStream(caminho));
        ImageView imageView = new ImageView(imagem);
        imageView.setFitWidth(LARGURA_IMAGEM_ERRO);
        imageView.setFitHeight(ALTURA_IMAGEM_ERRO);

        double centroX = (telaFase.getWidth() - LARGURA_IMAGEM_ERRO) / 2;
        double centroY = (telaFase.getHeight() - ALTURA_IMAGEM_ERRO) / 2;
        imageView.setX(centroX);
        imageView.setY(centroY);

        return imageView;
    }

    /**
     * Configura e inicia a transição de desaparecimento para uma imagem.
     * @param imageView A ImageView que desaparecerá.
     */
    private void configurarTransicaoDesaparecimento(ImageView imageView) {
        PauseTransition pt = new PauseTransition(DURACAO_IMAGEM_ERRO);
        pt.setOnFinished(e -> {
            telaFase.getChildren().remove(imageView);
            if (imageView == imagemErro) {
                imagemErro = null;
            } else if (imageView == imagemMiss) {
                imagemMiss = null;
            }
        });
        pt.play();
    }
}