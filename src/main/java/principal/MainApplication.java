package principal;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class MainApplication extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        FXMLLoader menu = new FXMLLoader(getClass().getResource("/menu/menu-principal-view.fxml"));

        Scene scene = new Scene(menu.load(), 1236, 804); //

        //icone do jar
        Image icon = new Image(getClass().getResourceAsStream("/assets/icones/icone2.png"));
        mainStage.getIcons().add(icon);

        mainStage.setTitle("Tão Tão Dançante");
        mainStage.setScene(scene);

        mainStage.setResizable(false);
        mainStage.show();
    }
    public static void trocarTela(String fxmlFile) {
        try {
            Parent currentRoot = mainStage.getScene().getRoot();
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), currentRoot);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
                    Parent newRoot = loader.load();
                    Scene newScene = new Scene(newRoot, 1236, 804);

                    newRoot.setOpacity(0.0); // Prepara para o fade in
                    mainStage.setScene(newScene);

                    FadeTransition fadeIn = new FadeTransition(Duration.millis(400), newRoot);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fadeOut.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}