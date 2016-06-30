package com.example.jackson.gameuebung_3.collision;

import com.example.jackson.gameuebung_3.math.Vector3;

public class Sphere implements Shape3D {

	private Vector3 center;
	private float radius;

	public Sphere() {
		this.center = new Vector3();
		this.radius = 0.0f;
	}

	public Sphere(Vector3 center, float radius) {
		this.center = new Vector3(center.v[0], center.v[1], center.v[2]);
		this.radius = radius;
	}

	public Sphere(float x, float y, float z, float radius) {
		this.center = new Vector3(x, y, z);
		this.radius = radius;
	}
	
	public boolean intersects(Shape3D shape) {
		return shape.intersects(this);
	}


	public boolean intersects(Sphere sphere) {
		float distSqr = Vector3.subtract(sphere.center, this.center).getLengthSqr();
		return distSqr <= (this.radius + sphere.radius) * (this.radius + sphere.radius);
	}

	
	public Vector3 getPosition() {
		return center;
	}


	public void setPosition(Vector3 position) {
		this.center.v[0] = position.v[0];
		this.center.v[1] = position.v[1];
		this.center.v[2] = position.v[2];
	}

	public Vector3 getCenter() {
		return center;
	}

	public void setCenter(Vector3 center) {
		this.center = center;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

}
