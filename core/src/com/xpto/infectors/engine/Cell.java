package com.xpto.infectors.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cell extends Circle {
	public static final float MIN_RADIUS = 20;
	public static final float MAX_RADIUS = 100;

	private static Texture img;

	public Cell(Team _team) {
		super();

		if (img == null)
			img = new Texture("circle.png");

		setTeam(_team);
	}

	public void render(SpriteBatch batch) {
		Team t = getTeam();
		float r2 = getRadius() * 2;

		batch.setColor(t.getR(), t.getG(), t.getB(), t.getA());
		batch.draw(img, getX() - getRadius(), getY() - getRadius(), r2, r2);
	}
}
