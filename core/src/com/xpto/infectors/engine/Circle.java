package com.xpto.infectors.engine;

import com.badlogic.gdx.math.Vector2;

public abstract class Circle {
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

    public Circle() {
        position = Vector2.Zero;
        radius = 0;
    }

    public static Vector2 direction(Circle _circle1, Circle _circle2) {
        _circle1.position.crs(v)
        
        float d = distance(_circle1, _circle2);
        return d <= ((_circle1.radius + _circle2.radius) * 1.1f);
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
