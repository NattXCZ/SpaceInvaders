module com.game.spaceinvaders {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.game.spaceinvaders to javafx.fxml;
    exports com.game.spaceinvaders;
}