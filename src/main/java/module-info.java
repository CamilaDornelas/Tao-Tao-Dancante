module demo { 
    requires javafx.controls;
    requires javafx.fxml;

    opens jogo to javafx.fxml;
    exports jogo;

  
    opens transicao1 to javafx.fxml;
    exports jogo.controllers;
    opens jogo.controllers to javafx.fxml;


}
//testeAline