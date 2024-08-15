module com.game.spaceinvaders {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.game.spaceinvaders to javafx.fxml;
    exports com.game.spaceinvaders;
}