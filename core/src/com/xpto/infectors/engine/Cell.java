package com.xpto.infectors.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cell extends Circle {
    public static final float MIN_RADIUS = 20;
    public static final float MAX_RADIUS = 100;

    private static Texture img;

    private float energy;
    private float energyRadius;

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float _energy) {
        if (_energy < 0) {
            energy = 0;
            energyRadius = 0;
        } else {
            energy = _energy;
            energyRadius = (float) Math.sqrt(energy / Math.PI);
        }
    }

    public Cell(Team _team) {
        super();

        if (img == null)
            img = new Texture("circle.png");

        setTeam(_team);
    }

    public void render(SpriteBatch batch) {
        Team t = getTeam();
        float r2 = getRadius() * 2;
        float er2 = energyRadius * 2;

        batch.setColor(t.getR(), t.getG(), t.getB(), t.getA());
        batch.draw(img, getX() - getRadius(), getY() - getRadius(), r2, r2);
        batch.draw(img, getX() - energyRadius, getY() - energyRadius, er2, er2);
    }

    @Override
    public void update(float seconds) {
        super.update(seconds);

        energy += (getRadius() / MAX_RADIUS) * getTeam().getRegenerate() * seconds;
        energyRadius = (float) Math.sqrt(energy / Math.PI);
    }
}
