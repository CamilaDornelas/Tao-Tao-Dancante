package jogo;

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
        mainStage = stage; // stage principal

        // carregando o FXML do menu principal
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/menu/menu-principal-view.fxml"));

        //tamanho da janela
        Scene scene = new Scene(fxmlLoader.load(), 1236, 804); //

        //icone do jar
        Image icon = new Image(getClass().getResourceAsStream("/assets/icones/icone2.png"));
        mainStage.getIcons().add(icon);

        mainStage.setTitle("Tão Tão Dançante"); //título da janela
        mainStage.setScene(scene);
        mainStage.setResizable(false); //não pode redimensionar a janela
        mainStage.show();
    }


    public static void showTransicao1Screen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/transicao1/transicao1-view.fxml"));
        Scene transicao1Scene = new Scene(fxmlLoader.load(), 1236, 804);
        mainStage.setScene(transicao1Scene);
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    private static MainApplication instance;
    public MainApplication() {
        instance = this;
    }
    public static MainApplication getInstanceReference() {
        return instance;
    }
}