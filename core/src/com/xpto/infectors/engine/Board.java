package com.xpto.infectors.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.xpto.infectors.Global;

public class Board extends ScreenAdapter {
    private static float collision = 10f;

    private Global game;

    private boolean cellsSort = true;
    private ArrayList<Cell> cells;

    private Cell selected;
    private Texture ring;
    private BitmapFont font;

    private static Queue<Cell> poolCells = new LinkedList<Cell>();

    private static Cell getCellFromPool(Team _team, Vector2 _position, float _radius, float _energy) {
        Cell c = poolCells.poll();

        if (c == null)
            c = new Cell(_team);
        else
            c.setTeam(_team);

        c.setPosition(_position);
        c.setRadius(_radius);
        c.setEnergy(_energy);

        return c;
    }

    private ArrayList<Attack> attacks;

    private static Queue<Attack> poolAttacks = new LinkedList<Attack>();

    private static Attack getAttackFromPool(Team _team, Vector2 _position, float _energy, Cell _target) {
        Attack a = poolAttacks.poll();
        if (a == null)
            a = new Attack();

        a.setTeam(_team);
        a.setPosition(_position);
        a.setEnergy(_energy);
        a.setTarget(_target);

        return a;
    }

    public Board(Global _game) {
        game = _game;

        game.setBackground(0, 0, 0, 1);

        cells = new ArrayList<Cell>();
        attacks = new ArrayList<Attack>();

        // Assets
        ring = new Texture("ring.png");

        font = new BitmapFont();
        font.setColor(1, 1, 1, 1);

        // Load scenario
        for (int i = 0; i < 15; i++) {
            float rad = new Random().nextFloat() * (Cell.MAX_RADIUS - Cell.MIN_RADIUS) + Cell.MIN_RADIUS;

            // Rnd teams
            Team t = null;
            switch (i) {
            case 0:
                t = new Team.Blue();
                break;
            case 1:
                t = new Team.Red();
                break;
            case 2:
                t = new Team.Green();
                break;
            }

            cells.add(getCellFromPool(t, new Vector2(new Random().nextFloat() * Global.WIDTH, new Random().nextFloat()
                    * Global.HEIGHT), rad, new Random().nextFloat() * rad));
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        draw();
        update();
    }

    private void draw() {
        Color color = game.batch().getColor();
        SpriteBatch batch = game.batch();

        // Cells
        for (int i = 0; i < cells.size(); i++)
            cells.get(i).render(batch);

        batch.setColor(color.r, color.g, color.b, color.a);

        if (selected != null) {
            float adjust = selected.getRadius() * 0.6f;
            batch.draw(ring, selected.getX() - adjust - selected.getRadius(),
                    selected.getY() - adjust - selected.getRadius(), 2 * (selected.getRadius() + adjust),
                    2 * (selected.getRadius() + adjust));

            String v1 = (int) (selected.getEnergy() / 10) + " / " + (int) (selected.getArea() / 10);
            TextBounds tb = font.getBounds(v1);
            font.draw(game.batch(), v1, selected.getX() - tb.width / 2, selected.getY() + tb.height / 2);
        }
    }

    private long lastUpdate;

    private void update() {
        float seconds = 0;
        if (lastUpdate == 0)
            lastUpdate = System.currentTimeMillis();
        else {
            seconds = (System.currentTimeMillis() - lastUpdate) / 1000f;
            lastUpdate = System.currentTimeMillis();
        }

        // Sort cells
        // With this sort collisions of cells, may be fast tested
        if (cellsSort) {
            cellsSort = false;

            Cell c1;
            Cell c2;

            // Bubble
            for (int i = 0; i < cells.size() - 1; i++) {
                c1 = cells.get(i);
                for (int j = i + 1; j < cells.size(); j++) {
                    c2 = cells.get(j);

                    if (c1.getX() > c2.getX()) {
                        cells.remove(j);
                        cells.add(i, c2);
                    }
                }
            }
        }

        // Test cell collisions
        for (int x1 = 0; x1 < cells.size(); x1++) {
            Cell c1 = cells.get(x1);

            // Back
            for (int x2 = x1 - 1; x2 > 0; x2--)
                if (!cellCollisionTest(c1, cells.get(x2), seconds))
                    break;

            // Front
            for (int x2 = x1 + 1; x2 < cells.size(); x2++)
                if (!cellCollisionTest(c1, cells.get(x2), seconds))
                    break;
        }

        // Make the cells move
        for (int x1 = 0; x1 < cells.size(); x1++)
            cells.get(x1).update(seconds);

        // Test cell X attack collisions
        // Note that here there is a problem
        // :: every cell is tested against every attack
        // TODO: optimize algorithm to consider X distance and do less tests
        for (int i = 0; i < cells.size(); i++) {
            Cell c = cells.get(i);

            for (int j = 0; j < attacks.size(); j++) {
                Attack a = attacks.get(j);

                cellCollisionTest(c, a, seconds);
            }
        }

        // User interactions
        if (Gdx.input.isTouched()) {
            Vector3 touch = new Vector3();
            touch.x = Gdx.input.getX();
            touch.y = Gdx.input.getY();
            game.camera().unproject(touch);

            // if (selected == null) {
            // Select cell
            for (int i = 0; i < cells.size(); i++) {
                Cell c = cells.get(i);
                if (c.contains(touch.x, touch.y)) {
                    selected = cells.get(i);
                    break;
                }
            }
            // }
        }
    }

    /**
     * Test collision and make cells move each other to make the collision stops Returns true when the cells are too far
     * to make any collision (based on x) and false otherwise
     */
    private boolean cellCollisionTest(Cell c1, Cell c2, float seconds) {
        if (c1.isNear(c2)) {
            // Set to reorder in next update
            cellsSort = true;

            // Set height
            float h = (c1.getRadius() / (c1.getRadius() + c2.getRadius())) * collision;

            // TODO: Correct direction calc
            Vector2 m = c2.direction(c1);
            Vector2 m1 = new Vector2(m.x * h * seconds, m.y * h * seconds);
            Vector2 m2 = new Vector2(m.x * (collision - h) * seconds, m.y * (collision - h) * seconds);

            if (c1.isColliding(c2)) {
                // Move 3x when collision
                m1.x *= 3;
                m1.y *= 3;
                m2.x *= 3;
                m2.y *= 3;
            }

            c1.addMovment(m1);
            c2.subMovment(m2);
        } else if (Math.abs(c1.getX() - c2.getX()) > (c1.getRadius() + Cell.MAX_RADIUS) * 1.1f)
            // Too far
            return false;
        return true;
    }

    private boolean cellCollisionTest(Cell c, Attack a, float seconds) {
        if (c.getTeam() != a.getTeam() && c.isColliding(a)) {
            float en = c.getEnergy() - a.getEnergy();
            if (en < 0) {
                // The energy removed is bigger, stole the cell
                c.setTeam(a.getTeam());
                c.setEnergy(-en);
            } else
                // Causes damage
                c.setEnergy(en);

            // Destroy attack and send it to pool
            attacks.remove(a);
            poolAttacks.add(a);
        } else if (Math.abs(c.getX() - a.getX()) > c.getRadius() + Attack.DEFAULT_RADIUS)
            return false;
        return true;
    }
}
