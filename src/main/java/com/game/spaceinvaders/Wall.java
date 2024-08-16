package com.game.spaceinvaders;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Wall{


    private final BooleanProperty active;
    private final IntegerProperty lives;
    private final IntegerProperty row;
    private final IntegerProperty column;
    private final IntegerProperty width;
    private final IntegerProperty height;

    public Wall(int row, int column, int width, int height) {
        this.active = new SimpleBooleanProperty(true);
        this.lives  =  new SimpleIntegerProperty(8);
        this.column = new SimpleIntegerProperty(column);   //x
        this.row = new SimpleIntegerProperty(row);   //y
        this.width = new SimpleIntegerProperty(width);
        this.height = new SimpleIntegerProperty(height);
    }

    public void minusLive(){
        this.setLives( getLives() - 1);
        this.setWidth(getWidth() - 10);
        this.setColumn(getColumn() + 5);

        if(getLives() <= 0) setActive(false);
    }


    //active
    public final BooleanProperty activeProperty() {
        return this.active;
    }
    public final boolean isActive() {
        return this.activeProperty().get();
    }
    public final void setActive(final boolean active) {
        this.activeProperty().set(active);
    }

    //row
    public final IntegerProperty rowProperty() {
        return this.row;
    }
    public final int getRow() {
        return this.rowProperty().get();
    }
    public final void setRow(final int row) {
        this.rowProperty().set(row);
    }

    //col
    public final IntegerProperty columnProperty() {
        return this.column;
    }
    public final int getColumn() {
        return this.columnProperty().get();
    }
    public final void setColumn(final int column) {
        this.columnProperty().set(column);
    }

    //height
    public final IntegerProperty heightProperty() {
        return this.height;
    }
    public final int getHeight() {
        return this.heightProperty().get();
    }
    public final void setHeight(final int height) {
        this.heightProperty().set(height);
    }

    //width
    public final IntegerProperty widthProperty() {
        return this.width;
    }
    public final int getWidth() {
        return this.widthProperty().get();
    }
    public final void setWidth(final int width) {
        this.widthProperty().set(width);
    }

    //lives
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
