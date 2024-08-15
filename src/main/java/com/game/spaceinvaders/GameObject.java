package com.game.spaceinvaders;


import javafx.beans.property.*;

public class GameObject {

    private final BooleanProperty active;
    private final DoubleProperty x;
    private final DoubleProperty y;
    private final IntegerProperty size;


    //trida pro monstra i hrace
    public GameObject(int x, int y, int size) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.size = new SimpleIntegerProperty(size);
        this.active = new SimpleBooleanProperty(true);
    }
    //x
    public final DoubleProperty XProperty() {
        return this.x;
    }
    public final double getX() {
        return this.XProperty().get();
    }
    public final void setX(final double x) {
        this.XProperty().set(x);
    }

    //y
    public final DoubleProperty YProperty() {
        return this.y;
    }
    public final double getY() {
        return this.YProperty().get();
    }
    public final void setY(final double y) {
        this.YProperty().set(y);
    }

    //size
    public final IntegerProperty sizeProperty() {
        return this.size;
    }
    public final int getSize() {
        return this.sizeProperty().get();
    }
    public final void setSize(final int size) {
        this.sizeProperty().set(size);
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


}
