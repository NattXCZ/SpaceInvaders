package com.game.spaceinvaders;


import javafx.beans.property.*;

public class GameObject {

    //TODO: možná udělat "Wall" tridu jako podtridu GameObject (oproti tomu ma navic jen lives

    private final BooleanProperty active;
    private final DoubleProperty row;
    private final DoubleProperty col;


    //private final IntegerProperty size;
    private final IntegerProperty width;
    private final IntegerProperty height;


    //TODO: kouknout proc je nekde "int" a v this je  SimpleDoubleProperty
    //trida pro monstra i hrace
    public GameObject(int row, int col, int width, int height) {
        this.row = new SimpleDoubleProperty(row);
        this.col = new SimpleDoubleProperty(col);
        //this.size = new SimpleIntegerProperty(size);
        this.active = new SimpleBooleanProperty(true);

        this.width = new SimpleIntegerProperty(width);
        this.height = new SimpleIntegerProperty(height);
    }
    //x
    public final DoubleProperty rowProperty() {
        return this.row;
    }
    public final double getRow() {
        return this.rowProperty().get();
    }
    public final void setRow(final double row) {
        this.rowProperty().set(row);
    }

    //y
    public final DoubleProperty colProperty() {
        return this.col;
    }
    public final double getCol() {
        return this.colProperty().get();
    }
    public final void setCol(final double col) {
        this.colProperty().set(col);
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

}
