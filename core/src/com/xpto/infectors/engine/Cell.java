package com.xpto.infectors.engine;

public class Cell extends Circle {
    private static final long serialVersionUID = 2L;

    public static final float MIN_RADIUS = 20;
    public static final float MAX_RADIUS = 100;

    public Cell(Team _team) {
        super();

        setTeam(_team);
    }
}
