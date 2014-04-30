package com.xpto.infectors.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.xpto.infectors.Global;

public class Board extends ScreenAdapter {
    private Global game;

    private boolean cellsSort = true;
    private ArrayList<Cell> cells;
    private Cell selected;

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

    private Texture img;

    public Board(Global _game) {
        game = _game;

        game.setBackground(0, 0, 0, 1);

        cells = new ArrayList<Cell>();
        attacks = new ArrayList<Attack>();

        // Assets
        img = new Texture("circle.png");

        // Load scenario
        for (int i = 0; i < 5; i++) {
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

        // Cells
        for (int i = 0; i < cells.size(); i++) {
            Cell c = cells.get(i);
            Team t = c.getTeam();

            game.batch().setColor(t.getR(), t.getG(), t.getB(), t.getA());
            game.batch().draw(img, c.getX() - c.getRadius(), c.getY() - c.getRadius(), c.getRadius() * 2,
                    c.getRadius() * 2);
        }

        game.batch().setColor(color.r, color.g, color.b, color.a);
    }

    private void update() {
        // Sort cells (using bubble)
        // With this sort collisions of cells, may be fast tested
        if (cellsSort) {
            cellsSort = false;

            Cell c1;
            Cell c2;
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
        // Note that here there is a problem
        // :: middle cells collides more than others
        // TODO: distribute collisions equally
        for (int i = 0; i < cells.size(); i++) {
            Cell c1 = cells.get(i);

            for (int j = 0; j < cells.size(); j++)
                if (i != j) {
                    Cell c2 = cells.get(j);

                    if (!cellCollisionTest(c1, c2))
                        break;
                }
        }

        // Test cell X attack collisions
        // Note that here there is a problem
        // :: every cell is tested against every attack
        // TODO: optimize algorithm to consider X distance and do less tests
        for (int i = 0; i < cells.size(); i++) {
            Cell c = cells.get(i);

            for (int j = 0; j < attacks.size(); j++) {
                Attack a = attacks.get(j);

                cellCollisionTest(c, a);
            }
        }

        // User interactions
        if (Gdx.input.isTouched()) {
            int tX = Gdx.input.getX();
            int tY = Gdx.input.getY();

            if (selected == null) {
                // Select cell
                for (int i = 0; i < cells.size(); i++) {
                    Cell c = cells.get(i);
                    if (c.getTeam() == game.getUserTeam() && c.contains(tX, tY)) {
                        selected = cells.get(i);
                        break;
                    }
                }
            }
        }
    }

    private boolean cellCollisionTest(Cell c1, Cell c2) {
        if (c1.isNear(c2)) {
            // Set to reorder in next update
            cellsSort = true;

            Vector2 move = c2.direction(c1);

            // Move 1 nor direction if is near
            c1.setPosition(c1.getPosition().add(move));
            c2.setPosition(c2.getPosition().sub(move));

            if (c1.isColliding(c2)) {
                // Move +2x in collisions
                c1.setPosition(c1.getPosition().add(move).add(move));
                c2.setPosition(c2.getPosition().sub(move).sub(move));
            }
        } else if (Math.abs(c1.getX() - c2.getX()) > (c1.getRadius() + Cell.MAX_RADIUS) * 1.1f)
            return false;
        return true;
    }

    private boolean cellCollisionTest(Cell c, Attack a) {
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
