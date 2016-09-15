package com.example.jackson.gameuebung_3.collision;

import com.example.jackson.gameuebung_3.Mesh;
import com.example.jackson.gameuebung_3.graphics.Material;
import com.example.jackson.gameuebung_3.math.Matrix4x4;
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
	
	public boolean intersects(Shape3D shape, float orbit) {
		return shape.intersects(this, orbit);
	}

	public boolean intersects(Sphere sphere, float orbit) {
		float distSqr = Vector3.subtract(sphere.center, this.center).getLengthSqr();
		float test =  (this.radius + sphere.radius) * (this.radius + sphere.radius);
//		if( distSqr <= (this.radius + sphere.radius) * (this.radius + sphere.radius) ){
			//Log.e("distSqr", String.valueOf(distSqr)+" und "+String.valueOf(test));
//		}
		float x = 0;
        if(orbit == 12f || orbit == -12f){
           x = 38.5f;
		}
		if(orbit == 0.5f){
			x = -2f;
		}
		return (distSqr - x) <= (this.radius + sphere.radius) * (this.radius + sphere.radius);
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

	public void addData(Mesh mesh, Material material){
		this.material = material;
		this.mesh = mesh;
	}

	public Mesh mesh;
	public Material material;

	public Mesh getMesh() {
		return mesh;
	}

	public Material getMaterial() {
		return material;
	}

	@Override
	public Matrix4x4 getPositionAsMatrix() {
		float[] m = new float[16]; // Array mit 16 Feldern
		m[0] = 1; m[4] = 1; m[ 8] = 1; m[12] = this.center.getX();
		m[1] = 1; m[5] = 1; m[ 9] = 1; m[13] = this.center.getY();
		m[2] = 1; m[6] = 1; m[10] = 1; m[14] = this.center.getZ();
		m[3] = 1; m[7] = 1; m[11] = 1; m[15] = 1;

		Matrix4x4 matrix = new Matrix4x4(m);
		matrix.scale(3f);
		return  matrix;
	}


}
