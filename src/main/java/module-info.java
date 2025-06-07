module demo { 
    requires javafx.controls;
    requires javafx.fxml;

    opens menu to javafx.fxml; 
    exports menu;                

  
    opens transicao1 to javafx.fxml;   
    exports transicao1;                


}
//testeAline