package com.xpto.infectors.engine;

import com.badlogic.gdx.math.Vector2;

public abstract class Circle {
	private Vector2 position;
	private Vector2 toMove;

	public void addMovment(Vector2 _newMove) {
		toMove.add(_newMove);
	}

	public void subMovment(Vector2 _newMove) {
		toMove.sub(_newMove);
	}

	public void move() {
		position.add(toMove);
		toMove.sub(toMove);
	}

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
		if (team == null)
			team = new Team.Gray();
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
		position = new Vector2();
		toMove = new Vector2();
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

	public boolean contains(float x, float y) {
		x = position.x - x;
		y = position.y - y;
		return x * x + y * y <= radius * radius;
	}

	public boolean contains(Vector2 point) {
		float dx = position.x - point.x;
		float dy = position.y - point.y;
		return dx * dx + dy * dy <= radius * radius;
	}

	public boolean contains(Circle c) {
		float dx = position.x - c.position.x;
		float dy = position.y - c.position.y;
		// The distance to the furthest point on circle c is the distance
		// between the center of the two circles plus the radius.
		// We use the squared distance so we can avoid a sqrt.
		float maxDistanceSqrd = dx * dx + dy * dy + c.radius * c.radius;
		return maxDistanceSqrd <= radius * radius;
	}

	public static Vector2 direction(Circle _circle1, Circle _circle2) {
		Vector2 v1 = new Vector2(_circle1.position);
		Vector2 v2 = new Vector2(_circle2.position);

		return v1.lerp(v2, 1).nor();
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
