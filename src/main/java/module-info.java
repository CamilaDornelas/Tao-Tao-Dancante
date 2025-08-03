module demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;

    opens jogo to javafx.fxml;
    exports jogo;

    opens transicao1 to javafx.fxml;
    exports jogo.controllers;
    opens jogo.controllers to javafx.fxml;

    opens jogo.fases to javafx.fxml;
}
