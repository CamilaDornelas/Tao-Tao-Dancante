package menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.application.Platform;

public class MenuController {

    @FXML
    private Button startButton;

    @FXML
    private Button exitButton;

    @FXML
    private void handleStartButtonAction(ActionEvent event) {
        System.out.println("Botão COMEÇAR clicado!");
        // Lógica para iniciar o jogo ou ir para a próxima tela
        // Ex: menu.MainApplication.getMainStage().setScene(new GameScene());
    }

    @FXML
    private void handleExitButtonAction(ActionEvent event) {
        System.out.println("Botão SAIR clicado!");
        Platform.exit();
    }

    @FXML
    private void initialize() {
        // Se o CSS está funcionando, essas linhas podem ser removidas daqui.
        // Elas garantem transparência caso o CSS não seja aplicado por algum motivo.
        startButton.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-border-color: transparent;");
        exitButton.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-border-color: transparent;");

        // Garante que não haja texto visível no botão
        startButton.setText("");
        exitButton.setText("");
    }
}