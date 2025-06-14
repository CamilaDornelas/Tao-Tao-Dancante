package jogo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage; // stage principal

        // carregando o FXML do menu principal
        FXMLLoader menu = new FXMLLoader(getClass().getResource("/menu/menu-principal-view.fxml"));

        //tamanho da janela
        Scene scene = new Scene(menu.load(), 1236, 804); //

        //icone do jar
        Image icon = new Image(getClass().getResourceAsStream("/assets/icones/icone2.png"));
        mainStage.getIcons().add(icon);

        mainStage.setTitle("Tão Tão Dançante"); //título da janela
        mainStage.setScene(scene);

        mainStage.setResizable(false); //não pode redimensionar a janela
        mainStage.show();
    }


    public static void showTransicao1Screen() throws IOException {
        FXMLLoader transicao1 = new FXMLLoader(MainApplication.class.getResource("/transicao1/transicao1-view.fxml"));
        Scene transicao1Scene = new Scene(transicao1.load(), 1236, 804);
        mainStage.setScene(transicao1Scene);
    }

    public static void main(String[] args) {
        launch();
    }
}