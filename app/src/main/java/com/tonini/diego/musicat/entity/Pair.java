package com.tonini.diego.musicat.entity;

/**
 * Created by Diego on 25/07/2015.
 */
public class Pair<X,Y> {

    X x;
    Y y;
    public Pair(X x, Y y){
        this.x = x;
        this.y = y;
    }

    public void setX(X x) {
        this.x = x;
    }

    public void setY(Y y) {
        this.y = y;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }
}
