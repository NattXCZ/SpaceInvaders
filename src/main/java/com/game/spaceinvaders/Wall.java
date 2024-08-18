package com.game.spaceinvaders;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Wall extends GameObject {

    private final IntegerProperty lives;

    public Wall(int row, int column, int width, int height) {
        super(row, column, width, height);
        this.lives = new SimpleIntegerProperty(8);
    }

    public void minusLive(){
        this.setLives( getLives() - 1);
        this.setWidth(getWidth() - 10);
        this.setCol(getCol() + 5);

        if(getLives() <= 0) setActive(false);
    }

    public final IntegerProperty livesProperty() {
        return this.lives;
    }

    public final int getLives() {
        return this.livesProperty().get();
    }

    public final void setLives(final int lives) {
        this.livesProperty().set(lives);
    }
}

