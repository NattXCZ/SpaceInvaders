package com.game.spaceinvaders;


import javafx.beans.property.*;

public class GameObject {
    private final BooleanProperty active;
    private final IntegerProperty row;
    private final IntegerProperty col;

    private final IntegerProperty width;
    private final IntegerProperty height;

    public GameObject(int row, int col, int width, int height) {
        this.row = new SimpleIntegerProperty(row);
        this.col = new SimpleIntegerProperty(col);
        //this.size = new SimpleIntegerProperty(size);
        this.active = new SimpleBooleanProperty(true);

        this.width = new SimpleIntegerProperty(width);
        this.height = new SimpleIntegerProperty(height);
    }

    public final IntegerProperty rowProperty() {
        return this.row;
    }
    public final int getRow() {
        return this.rowProperty().get();
    }
    public final void setRow(final int row) {
        this.rowProperty().set(row);
    }

    public final IntegerProperty colProperty() {
        return this.col;
    }
    public final int getCol() {
        return this.colProperty().get();
    }
    public final void setCol(final int col) {
        this.colProperty().set(col);
    }

    public final BooleanProperty activeProperty() {
        return this.active;
    }
    public final boolean isActive() {
        return this.activeProperty().get();
    }
    public final void setActive(final boolean active) {
        this.activeProperty().set(active);
    }

    public final IntegerProperty heightProperty() {
        return this.height;
    }
    public final int getHeight() {
        return this.heightProperty().get();
    }
    public final void setHeight(final int height) {
        this.heightProperty().set(height);
    }

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
