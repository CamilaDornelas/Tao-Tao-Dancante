module demo { // O nome do seu módulo. Mantenha 'demo' se não mudou.
    requires javafx.controls;
    requires javafx.fxml;
    // requires javafx.graphics; // Mantenha se estiver usando ícones ou outros gráficos

    // Pacote do menu (MainApplication e MenuController)
    opens menu to javafx.fxml; // Permite que o FXMLLoader acesse classes do pacote 'menu'
    exports menu;                // Exporta o pacote 'menu' para ser visível a outros módulos

    // Pacote do jogo (GameController)
    opens transicao1 to javafx.fxml;   // Permite que o FXMLLoader acesse classes do pacote 'transicao1'
    exports transicao1;                // Exporta o pacote 'transicao1'


}