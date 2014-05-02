package com.xpto.infectors.engine;

public class Attack extends Circle {
    public static final float DEFAULT_RADIUS = 15;

    private Cell target;

    public Cell getTarget() {
        return target;
    }

    public void setTarget(Cell _target) {
        target = _target;
    }

    public Attack() {
        super();
        setRadius(DEFAULT_RADIUS);
    }
}
