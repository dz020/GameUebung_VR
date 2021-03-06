package com.example.jackson.gameuebung_3.collision;

import com.example.jackson.gameuebung_3.math.Vector2;

public class Point implements Shape2D {

	private Vector2 position;
	
	public Point() {
		this.position = new Vector2();
	}
	
	public Point(float x, float y) {
		this.position = new Vector2(x, y);
	}
	
	public Point(Vector2 position) {
		this.position = new Vector2(position.v[0], position.v[1]);
	}
	
	public boolean intersects(Shape2D shape) {
		return shape.intersects(this);
	}

	public boolean intersects(Point point) {
		return 0.0000001 > Vector2.subtract(point.position, this.position).getLengthSqr();
	}
	
	public boolean intersects(Circle circle) {
		float r = circle.getRadius();
		float distSqr = Vector2.subtract(circle.getCenter(), this.position).getLengthSqr(); 
		return distSqr <= r * r;
	}

	public boolean intersects(AABB box) {
		return !(this.position.getX() < box.getMin().getX() || this.position.getX() > box.getMax().getX()) && !(this.position.getY() < box.getMin().getY() || this.position.getY() > box.getMax().getY());
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position.v[0] = position.v[0];
		this.position.v[1] = position.v[1];
	}

	public float getX(){
		return position.getX();
	}

	public float getY(){
		return position.getY();
	}

}
