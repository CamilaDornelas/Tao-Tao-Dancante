package transicao1; // Pacote do novo controller

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class Transicao1Controller {

    @FXML
    private Button danceButton; // O botão "CLIQUE PARA DANÇAR" do FXML

    @FXML
    private void initialize() {
        System.out.println("Cena Transicao1 (transicao.png) carregada!");
    }

    @FXML
    private void handleDanceButtonClick(ActionEvent event) {
        System.out.println("CLIQUE PARA DANÇAR! AÇÃO DE DANÇA!");
        // AQUI você colocaria a lógica para a próxima fase do jogo
    }
}