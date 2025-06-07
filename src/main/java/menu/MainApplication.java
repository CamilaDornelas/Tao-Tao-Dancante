package menu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    private static Stage mainStage; // renomeado

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage; // Armazena o Stage principal

        // carrega o FXML do menu principal
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/menu/menu-principal-view.fxml"));

        //tamanho da janela
        Scene scene = new Scene(fxmlLoader.load(), 1236, 804); // Exemplo: 800 largura, 600 altura

        //icone do jar
        Image icon = new Image(getClass().getResourceAsStream("/icones/icone2.png"));
        mainStage.getIcons().add(icon);

        mainStage.setTitle("Tão Tão Dançante"); //título da janela
        mainStage.setScene(scene);
        mainStage.setResizable(false); //impede que o usuário redimensione a janela
        mainStage.show();
    }

    // metodo para mudar para a tela do jogo (ainda não implementado, mas é o próximo passo)
    // este metodo seria chamado de dentro do MenuController.
    // public void showGameScreen() throws IOException {
    //     // Exemplo:
    //     // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game/game-view.fxml"));
    //     // Scene gameScene = new Scene(fxmlLoader.load(), 800, 600);
    //     // mainStage.setScene(gameScene);
    //     // mainStage.setTitle("Tão Tão Dançante - Jogo");
    // }

    public static void main(String[] args) {
        launch(); // Inicia a aplicação JavaFX
    }

    // Se precisar acessar o Stage principal ou métodos desta classe de outros controllers
    public static Stage getMainStage() {
        return mainStage;
    }

    // Opcional: Se você quiser um getInstance para a própria aplicação (menos comum para o Stage)
    private static MainApplication instance;
    public MainApplication() {
        instance = this;
    }
    public static MainApplication getInstanceReference() {
        return instance;
    }
}