package com.xpto.infectors.engine;

import com.badlogic.gdx.math.Vector2;

public abstract class Circle extends com.badlogic.gdx.math.Circle {
    private static final long serialVersionUID = 1L;

    private Vector2 position;

    public Vector2 getPosition() {
        return position;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setX(float _x) {
        position.x = _x;
    }

    public void setY(float _y) {
        position.y = _y;
    }

    private float radius;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        if (radius < 0)
            this.radius = 0;
        else
            this.radius = radius;
    }

    private Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    private float energy;

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public Circle() {
        position = Vector2.Zero;
        radius = 0;
    }

    public Vector2 direction(Circle _circle2) {
        return direction(this, _circle2);
    }

    public boolean isNear(Circle _circle2) {
        return isNear(this, _circle2);
    }

    public boolean isColliding(Circle _circle2) {
        return isColliding(this, _circle2);
    }

    public float distance(Circle _circle2) {
        return distance(this, _circle2);
    }

    public static Vector2 direction(Circle _circle1, Circle _circle2) {
        return _circle1.position.lerp(_circle2.position, 1).nor();
    }

    public static boolean isNear(Circle _circle1, Circle _circle2) {
        float d = distance(_circle1, _circle2);
        return d <= ((_circle1.radius + _circle2.radius) * 1.1f);
    }

    public static boolean isColliding(Circle _circle1, Circle _circle2) {
        float d = distance(_circle1, _circle2);
        return d <= (_circle1.radius + _circle2.radius);
    }

    public static float distance(Circle _circle1, Circle _circle2) {
        return _circle1.position.dst(_circle2.position);
    }
}
