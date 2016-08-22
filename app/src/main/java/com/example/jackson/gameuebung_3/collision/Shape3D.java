package com.example.jackson.gameuebung_3.collision;


import com.example.jackson.gameuebung_3.Mesh;
import com.example.jackson.gameuebung_3.graphics.Material;
import com.example.jackson.gameuebung_3.math.Matrix4x4;
import com.example.jackson.gameuebung_3.math.Vector3;

/**
 * Created by Jackson on 04.06.2016.
 */
public interface Shape3D {

    boolean intersects(Shape3D shape, float orbit);
    boolean intersects(Sphere sphere, float orbit);

    Vector3 getPosition();
    void setPosition(Vector3 position);

    void addData(Mesh modelMesh, Material modelMaterial);

    Mesh getMesh();
    Material getMaterial();

    Matrix4x4 getPositionAsMatrix();

}
